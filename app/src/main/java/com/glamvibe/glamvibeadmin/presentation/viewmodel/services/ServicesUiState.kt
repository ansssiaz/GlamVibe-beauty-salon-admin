package com.glamvibe.glamvibeadmin.presentation.viewmodel.services

import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.utils.Status

data class ServicesUiState(
    val services: List<Service>? = null,
    val filteredServices: List<Service> = emptyList(),
    val categories: List<String> = emptyList(),
    val lastSelectedCategory: String? = null,
    val status: Status = Status.Idle,
)