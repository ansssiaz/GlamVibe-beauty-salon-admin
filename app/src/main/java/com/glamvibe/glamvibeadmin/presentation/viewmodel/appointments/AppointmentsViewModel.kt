package com.glamvibe.glamvibeadmin.presentation.viewmodel.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.request.CommentForCancellation
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AppointmentsViewModel(
    private val appointmentsRepository: AppointmentsRepository
) : ViewModel() {
    private var _state = MutableStateFlow(AppointmentsUiState())
    val state = _state.asStateFlow()

    fun getAppointmentsForDate(year: Int, month: Int, day: Int) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val appointments = appointmentsRepository.getAppointmentsForDate(year, month, day)

                _state.update {
                    it.copy(
                        appointments = appointments,
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

    fun confirm(
        appointmentId: Int,
        appointmentDate: LocalDate,
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                appointmentsRepository.confirmAppointment(appointmentId)

                val appointments = appointmentsRepository.getAppointmentsForDate(
                    appointmentDate.year,
                    appointmentDate.monthNumber,
                    appointmentDate.dayOfMonth
                )

                _state.update {
                    it.copy(
                        appointments = appointments,
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

    fun complete(
        appointmentId: Int,
        appointmentDate: LocalDate,
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                appointmentsRepository.completeAppointment(appointmentId)

                val appointments = appointmentsRepository.getAppointmentsForDate(
                    appointmentDate.year,
                    appointmentDate.monthNumber,
                    appointmentDate.dayOfMonth
                )

                _state.update {
                    it.copy(
                        appointments = appointments,
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
        appointmentDate: LocalDate,
        comment: String
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val commentForCancellation = CommentForCancellation(text = comment)
                appointmentsRepository.cancelAppointment(appointmentId, commentForCancellation)

                val appointments = appointmentsRepository.getAppointmentsForDate(
                    appointmentDate.year,
                    appointmentDate.monthNumber,
                    appointmentDate.dayOfMonth
                )

                _state.update {
                    it.copy(
                        appointments = appointments,
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