package com.glamvibe.glamvibeadmin.presentation.viewmodel.clientAppointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.request.CommentForCancellation
import com.glamvibe.glamvibeadmin.domain.model.AppointmentStatus
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ClientAppointmentsViewModel(
    private val appointmentsRepository: AppointmentsRepository,
    private val clientId: Int
) : ViewModel() {
    private var _state = MutableStateFlow(ClientAppointmentsUiState())
    val state = _state.asStateFlow()

    init {
        getAppointments()
    }

    fun getAppointments() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val appointments = appointmentsRepository.getAppointmentsForClient(clientId)

                val currentAppointments =
                    appointments.filter { it.status == AppointmentStatus.IN_PROCESSING || it.status == AppointmentStatus.WAITING }

                val lastAppointments =
                    appointments.filter {
                        it.status == AppointmentStatus.DONE || it.status == AppointmentStatus.CANCELLATION_BY_THE_CLIENT
                                || it.status == AppointmentStatus.CANCELLATION_BY_THE_ADMINISTRATOR
                    }

                _state.update {
                    it.copy(
                        currentAppointments = currentAppointments,
                        lastAppointments = lastAppointments,
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

    fun cancel(
        appointmentId: Int,
        comment: String
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val commentForCancellation = CommentForCancellation(text = comment)
                appointmentsRepository.cancelAppointment(appointmentId, commentForCancellation)

                val appointments = appointmentsRepository.getAppointmentsForClient(clientId)

                val currentAppointments =
                    appointments.filter { it.status == AppointmentStatus.IN_PROCESSING || it.status == AppointmentStatus.WAITING }

                val lastAppointments =
                    appointments.filter {
                        it.status == AppointmentStatus.DONE || it.status == AppointmentStatus.CANCELLATION_BY_THE_CLIENT
                                || it.status == AppointmentStatus.CANCELLATION_BY_THE_ADMINISTRATOR
                    }

                _state.update {
                    it.copy(
                        currentAppointments = currentAppointments,
                        lastAppointments = lastAppointments,
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
}