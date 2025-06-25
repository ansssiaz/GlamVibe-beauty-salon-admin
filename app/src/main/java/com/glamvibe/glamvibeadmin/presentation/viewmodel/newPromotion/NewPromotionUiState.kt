package com.glamvibe.glamvibeadmin.presentation.viewmodel.newPromotion

import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.utils.Status

data class NewPromotionUiState(
    val newPromotion: Promotion? = null,
    val promotionToEdit: Promotion? = null,
    val services: List<String> = emptyList(),
    val status: Status = Status.Idle
)