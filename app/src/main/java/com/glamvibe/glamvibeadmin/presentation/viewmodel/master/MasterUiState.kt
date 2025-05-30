package com.glamvibe.glamvibeadmin.presentation.viewmodel.master

import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.utils.Status

data class MasterUiState(
    val master: Master? = null,
    val status: Status = Status.Idle,
)
