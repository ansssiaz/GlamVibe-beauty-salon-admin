package com.glamvibe.glamvibeadmin.presentation.viewmodel.appointments

import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.utils.Status

data class AppointmentsUiState(
    val appointments: List<Appointment> = emptyList(),
    val status: Status =  Status.Idle
)
