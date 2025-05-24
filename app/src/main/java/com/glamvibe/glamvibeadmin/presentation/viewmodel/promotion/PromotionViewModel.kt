package com.glamvibe.glamvibeadmin.presentation.viewmodel.promotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.promotions.PromotionsRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PromotionViewModel(
    private val repository: PromotionsRepository,
    private val promotionId: Int
) : ViewModel() {
    private var _state = MutableStateFlow(PromotionUiState())
    val state = _state.asStateFlow()

    init {
        loadPromotionInformation()
    }

    private fun loadPromotionInformation() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val promotion = repository.getPromotion(promotionId)
                _state.update { state ->
                    state.copy(
                        promotion = promotion,
                        status = Status.Idle
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }
}