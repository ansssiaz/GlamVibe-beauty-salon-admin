package com.glamvibe.glamvibeadmin.presentation.viewmodel.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServicesViewModel(
    private val servicesRepository: ServicesRepository) : ViewModel() {
    private var _state = MutableStateFlow(ServicesUiState())
    val state = _state.asStateFlow()

    init {
        getServices()
    }

    fun getServices() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val services = servicesRepository.getServices()
                val categories = services.map { it.category }.distinct()

                val filtered = _state.value.lastSelectedCategory?.let { category ->
                    if (category == "Все категории") services
                    else services.filter { it.category == category }
                } ?: services

                _state.update {
                    it.copy(
                        services = services,
                        categories = categories,
                        filteredServices = filtered,
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

    fun filterServicesByCategory(category: String?) {
        _state.update { state ->
            val filtered = when {
                category == null || category == "Все категории" -> state.services ?: emptyList()
                state.services == null -> emptyList()
                else -> state.services.filter { it.category == category }
            }

            state.copy(
                filteredServices = filtered,
                lastSelectedCategory = category
            )
        }
    }

    fun deleteService(id: Int){
        _state.update { it.copy(status = Status.Loading) }
        viewModelScope.launch {
            try {
                servicesRepository.deleteService(id)
                val services = servicesRepository.getServices()
                val categories = services.map { it.category }.distinct()

                val filtered = _state.value.lastSelectedCategory?.let { category ->
                    if (category == "Все категории") services
                    else services.filter { it.category == category }
                } ?: services

                _state.update {
                    it.copy(
                        services = services,
                        categories = categories,
                        filteredServices = filtered,
                        lastSelectedCategory = null,
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