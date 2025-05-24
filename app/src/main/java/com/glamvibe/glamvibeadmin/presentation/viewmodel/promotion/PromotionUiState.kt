package com.glamvibe.glamvibeadmin.presentation.viewmodel.promotion

import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.utils.Status

data class PromotionUiState(
    val promotion: Promotion? = null,
    val status: Status = Status.Idle
)
