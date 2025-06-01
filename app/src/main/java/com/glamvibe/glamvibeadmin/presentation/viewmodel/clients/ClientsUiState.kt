package com.glamvibe.glamvibeadmin.presentation.viewmodel.clients

import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.utils.Status

data class ClientsUiState(
    val clients: List<User>? = null,
    val foundClients: List<User> = emptyList(),
    val status: Status = Status.Idle
)
