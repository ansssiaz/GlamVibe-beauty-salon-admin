package com.glamvibe.glamvibeadmin.presentation.viewmodel.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.promotions.PromotionsRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PromotionsViewModel(private val repository: PromotionsRepository) : ViewModel() {
    private var _state = MutableStateFlow(PromotionsUiState())
    val state = _state.asStateFlow()

    init {
        loadCurrentPromotions()
    }

    private fun loadCurrentPromotions() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val promotions = repository.getCurrentPromotions()

                _state.update {
                    it.copy(
                        promotions = promotions,
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