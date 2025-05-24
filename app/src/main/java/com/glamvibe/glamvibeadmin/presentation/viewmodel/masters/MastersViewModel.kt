package com.glamvibe.glamvibeadmin.presentation.viewmodel.masters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MastersViewModel(
    private val mastersRepository: MastersRepository
) : ViewModel() {
    private var _state = MutableStateFlow(MastersUiState())
    val state = _state.asStateFlow()

    init {
        loadMasters()
    }

    private fun loadMasters() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val masters = mastersRepository.getMasters()

                val categories = masters.flatMap { it.categories }.distinct()

                val filtered = _state.value.lastSelectedCategory?.let { category ->
                    if (category == "Все категории") masters
                    else masters.filter { it.categories == categories }
                } ?: masters

                _state.update {
                    it.copy(
                        masters = masters,
                        categories = categories,
                        filteredMasters = filtered,
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

    fun filterMastersByCategory(category: String?) {
        _state.update { state ->
            val filtered = when {
                category == null || category == "Все категории" -> state.masters
                state.masters.isEmpty() -> emptyList()
                else -> state.masters.filter { it.categories.contains(category) }
            }

            state.copy(
                filteredMasters = filtered,
                lastSelectedCategory = category
            )
        }
    }
}