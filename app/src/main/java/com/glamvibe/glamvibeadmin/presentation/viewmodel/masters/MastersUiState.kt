package com.glamvibe.glamvibeadmin.presentation.viewmodel.masters

import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.utils.Status

data class MastersUiState(
    val masters: List<Master> = emptyList(),
    val filteredMasters: List<Master> = emptyList(),
    val categories: List<String> = emptyList(),
    val lastSelectedCategory: String? = null,
    val status: Status = Status.Idle
)
