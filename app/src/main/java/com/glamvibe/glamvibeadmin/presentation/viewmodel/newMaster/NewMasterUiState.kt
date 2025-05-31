package com.glamvibe.glamvibeadmin.presentation.viewmodel.newMaster

import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.utils.Status

data class NewMasterUiState(
    val newMaster: Master? = null,
    val masterToEdit: Master? = null,
    val categories: List<String> = emptyList(),
    val status: Status = Status.Idle
)
