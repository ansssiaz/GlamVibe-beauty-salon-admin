package com.glamvibe.glamvibeadmin.presentation.viewmodel.promotions

import com.glamvibe.glamvibeadmin.domain.model.Promotion
import com.glamvibe.glamvibeadmin.utils.Status

data class PromotionsUiState(
    val promotions: List<Promotion>? = null,
    val filteredPromotions: List<Promotion> = emptyList(),
    val categories: List<String> = emptyList(),
    val lastSelectedCategory: String? = null,
    val status: Status = Status.Idle
)
