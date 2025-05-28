package com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator

import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.utils.Status

data class AdministratorUiState(
    val administrator: User? = null,
    val status: Status = Status.Idle,
){
    val isError: Boolean
        get() = status is Status.Error && administrator == null
}
