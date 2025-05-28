package com.glamvibe.glamvibeadmin.presentation.viewmodel.newAdministrator

import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.utils.Status

data class NewAdministratorUiState(
    val administrator: User? = null,
    val status: Status = Status.Idle,
) {
    val isError: Boolean
        get() = status is Status.Error && administrator == null
}