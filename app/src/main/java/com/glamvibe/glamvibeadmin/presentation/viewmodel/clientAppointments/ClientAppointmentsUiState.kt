package com.glamvibe.glamvibeadmin.presentation.viewmodel.clientAppointments

import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.utils.Status

data class ClientAppointmentsUiState(
    val currentAppointments: List<Appointment> = emptyList(),
    val lastAppointments: List<Appointment> = emptyList(),
    val status: Status =  Status.Idle
)
