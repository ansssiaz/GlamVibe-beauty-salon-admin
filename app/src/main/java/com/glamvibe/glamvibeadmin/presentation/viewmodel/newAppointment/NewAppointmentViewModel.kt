package com.glamvibe.glamvibeadmin.presentation.viewmodel.newAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.request.NewAppointment
import com.glamvibe.glamvibeadmin.domain.model.toWeekDay
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import com.glamvibe.glamvibeadmin.utils.Status
import com.glamvibe.glamvibeadmin.utils.plusMinutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class NewAppointmentViewModel(
    private val appointmentsRepository: AppointmentsRepository,
    private val servicesRepository: ServicesRepository,
    private val mastersRepository: MastersRepository,
    private val clientId: Int
) : ViewModel() {
    private var _state = MutableStateFlow(NewAppointmentUiState())
    val state = _state.asStateFlow()

    init {
        loadServicesAndMastersInformation()
    }

    private fun loadServicesAndMastersInformation() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val services = servicesRepository.getServices(clientId)

                val mastersWithAppointments =
                    mastersRepository.getMastersWithCurrentAppointments()

                val mastersNames = mastersWithAppointments.map {
                    if (it.patronymic != null) "${it.lastname} ${it.name} ${it.patronymic}" else "${it.lastname} ${it.name}"
                }

                _state.update { state ->
                    state.copy(
                        services = services,
                        masters = mastersWithAppointments,
                        mastersNames = mastersNames,
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun filterMastersByService(serviceName: String?) {
        _state.update { state ->
            val service = state.services.find { it.name == serviceName }

            val filtered = if (service != null) {
                when {
                    serviceName == null || serviceName == "Выберите услугу" -> state.masters
                    else -> state.masters.filter { it.categories.contains(service.category) }
                }

            } else emptyList()

            state.copy(
                filteredMasters = filtered,
                lastSelectedService = serviceName
            )
        }
    }

    fun onMasterSelected(masterName: String?) {
        val master = state.value.masters.find { m ->
            val fio =
                if (m.patronymic != null) "${m.lastname} ${m.name} ${m.patronymic}" else "${m.lastname} ${m.name}"
            fio == masterName
        }

        if (master != null) {
            val lastSelectedMaster =
                if (master.patronymic != null) "${master.lastname} ${master.name} ${master.patronymic}" else "${master.lastname} ${master.name}"

            _state.update {
                it.copy(
                    lastSelectedMaster = lastSelectedMaster,
                    masterSchedule = master.schedule,
                    masterCurrentAppointments = master.appointments
                )
            }
        } else {
            _state.update {
                it.copy(
                    lastSelectedMaster = null,
                    masterSchedule = emptyList(),
                    masterCurrentAppointments = emptyList()
                )
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _state.update {
            it.copy(selectedDate = date)
        }

        val workingDay =
            state.value.masterSchedule.find { it.weekDay == date.dayOfWeek.toWeekDay() }

        if (workingDay == null) {
            _state.update {
                it.copy(
                    availableSlots = emptyList(),
                )
            }
            return
        }

        val bookedTimes = state.value.masterCurrentAppointments
            .filter { it.date == date }
            .map { it.time }
        val slots = mutableListOf<LocalTime>()

        var slot = workingDay.startTime
        while (slot < workingDay.endTime) {
            if (!bookedTimes.contains(slot)) {
                slots.add(slot)
            }
            slot = slot.plusMinutes(60)
        }

        _state.update {
            it.copy(availableSlots = slots)
        }
    }

    fun onTimeSelected(time: LocalTime) {
        _state.update {
            it.copy(selectedTime = time)
        }
    }

    fun makeAppointment(
        date: LocalDate,
        time: LocalTime,
        weekDay: DayOfWeek
    ) {
        _state.update { it.copy(status = Status.Loading) }

        val service = state.value.services.find { it.name == state.value.lastSelectedService }

        val master = state.value.masters.find { m ->
            val fio =
                if (m.patronymic != null) "${m.lastname} ${m.name} ${m.patronymic}" else "${m.lastname} ${m.name}"
            fio == state.value.lastSelectedMaster
        }

        if (service == null || master == null) {
            val error = "Для записи выберите услугу и мастера"
            _state.update {
                it.copy(status = Status.Error(Throwable(message = error)))
            }
            return
        }

        viewModelScope.launch {
            try {
                val newAppointment = NewAppointment(
                    clientId = clientId,
                    serviceId = service.id ,
                    masterId = master.id,
                    date = date,
                    time = time,
                    weekDay = weekDay.toWeekDay()
                )

                val appointment = appointmentsRepository.makeAppointment(clientId, newAppointment)

                _state.update {
                    it.copy(
                        appointment = appointment,
                        status = Status.Idle,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }
}