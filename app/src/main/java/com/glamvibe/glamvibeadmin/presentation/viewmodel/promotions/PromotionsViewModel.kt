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
        getPromotions()
    }

    fun getPromotions() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val promotions = repository.getPromotions().sortedByDescending { it.endDate }

                val categories = promotions
                    .flatMap {
                        it.services.map { service -> service.category }
                    }
                    .distinct()

                val filtered = _state.value.lastSelectedCategory?.let { category ->
                    if (category == "Все категории") promotions
                    else promotions.filter {
                        it.services
                            .map { service -> service.category }
                            .distinct()
                            .contains(category)
                    }
                } ?: promotions

                _state.update {
                    it.copy(
                        promotions = promotions,
                        categories = categories,
                        filteredPromotions = filtered,
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

    fun filterPromotionsByCategory(category: String?) {
        _state.update { state ->
            val filtered = when {
                category == null || category == "Все категории" -> state.promotions ?: emptyList()
                state.promotions == null -> emptyList()
                else -> state.promotions.filter {
                    it.services
                        .map { service -> service.category }
                        .distinct()
                        .contains(category)
                }
            }

            state.copy(
                filteredPromotions = filtered,
                lastSelectedCategory = category
            )
        }
    }
}