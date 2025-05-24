package com.glamvibe.glamvibeadmin.presentation.viewmodel.promotions

import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.utils.Status

data class PromotionsUiState(
    val promotions: List<Promotion> = emptyList(),
    val status: Status = Status.Idle
)
