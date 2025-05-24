package com.glamvibe.glamvibeadmin.presentation.viewmodel.newAdministrator

import com.glamvibe.glamvibeadmin.domain.model.Administrator
import com.glamvibe.glamvibeadmin.utils.Status

data class NewAdministratorUiState(
    val administrator: Administrator? = null,
    val status: Status = Status.Idle,
) {
    val isError: Boolean
        get() = status is Status.Error && administrator == null
}