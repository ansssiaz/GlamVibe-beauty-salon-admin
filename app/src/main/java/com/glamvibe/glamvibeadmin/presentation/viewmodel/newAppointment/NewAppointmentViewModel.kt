package com.glamvibe.glamvibeadmin.presentation.viewmodel.newAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.request.NewAppointment
import com.glamvibe.glamvibeadmin.data.model.response.toUser
import com.glamvibe.glamvibeadmin.domain.model.toWeekDay
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository
import com.glamvibe.glamvibeadmin.domain.repository.clients.ClientsRepository
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
    private val clientsRepository: ClientsRepository,
    private val appointmentsRepository: AppointmentsRepository,
    private val servicesRepository: ServicesRepository,
    private val mastersRepository: MastersRepository,
) : ViewModel() {
    private var _state = MutableStateFlow(NewAppointmentUiState())
    val state = _state.asStateFlow()

    init {
        loadInformation()
    }

    private fun loadInformation() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val clients = clientsRepository.getClients().map {
                    it.toUser()
                }

                val clientsNames = clients.map {
                    if (it.patronymic != null) "${it.lastname} ${it.name} ${it.patronymic}" else "${it.lastname} ${it.name}"
                }

                val services = servicesRepository.getServices()

                val mastersWithAppointments =
                    mastersRepository.getMastersWithCurrentAppointments()

                val mastersNames = mastersWithAppointments.map {
                    if (it.patronymic != null) "${it.lastname} ${it.name} ${it.patronymic}" else "${it.lastname} ${it.name}"
                }

                _state.update { state ->
                    state.copy(
                        clients = clients,
                        clientsNames = clientsNames,
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

    fun onClientSelected(clientName: String?) {
        val client = state.value.clients.find { m ->
            val fio =
                if (m.patronymic != null) "${m.lastname} ${m.name} ${m.patronymic}" else "${m.lastname} ${m.name}"
            fio == clientName
        }

        if (client != null) {
            val lastSelectedClient =
                if (client.patronymic != null) "${client.lastname} ${client.name} ${client.patronymic}" else "${client.lastname} ${client.name}"

            _state.update {
                it.copy(
                    lastSelectedClient = lastSelectedClient,
                )
            }
        } else {
            _state.update {
                it.copy(
                    lastSelectedClient = null,
                )
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

        val client = state.value.clients.find { client ->
            val fio =
                if (client.patronymic != null) "${client.lastname} ${client.name} ${client.patronymic}" else "${client.lastname} ${client.name}"
            fio == state.value.lastSelectedClient
        }

        val service = state.value.services.find { it.name == state.value.lastSelectedService }

        val master = state.value.masters.find { m ->
            val fio =
                if (m.patronymic != null) "${m.lastname} ${m.name} ${m.patronymic}" else "${m.lastname} ${m.name}"
            fio == state.value.lastSelectedMaster
        }

        if (service == null || master == null || client == null) {
            val error = "Для записи выберите клиента, услугу и мастера"
            _state.update {
                it.copy(status = Status.Error(Throwable(message = error)))
            }
            return
        }

        viewModelScope.launch {
            try {
                val newAppointment = NewAppointment(
                    clientId = client.id,
                    serviceId = service.id,
                    masterId = master.id,
                    date = date,
                    time = time,
                    weekDay = weekDay.toWeekDay()
                )

                val appointment = appointmentsRepository.makeAppointment(newAppointment)

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