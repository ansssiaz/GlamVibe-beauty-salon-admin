package com.glamvibe.glamvibeadmin.presentation.viewmodel.newService

import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.utils.Status

data class NewServiceUiState(
    val newService: Service? = null,
    val serviceToEdit: Service? = null,
    val categories: List<String> = emptyList(),
    val status: Status = Status.Idle
)