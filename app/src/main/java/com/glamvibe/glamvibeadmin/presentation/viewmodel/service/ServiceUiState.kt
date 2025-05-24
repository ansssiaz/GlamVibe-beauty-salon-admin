package com.glamvibe.glamvibeadmin.presentation.viewmodel.service

import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.utils.Status

data class ServiceUiState(
    val service: Service? = null,
    val status: Status = Status.Idle,
)