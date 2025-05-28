package com.glamvibe.glamvibeadmin.presentation.viewmodel.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val servicesRepository: ServicesRepository,
    private val serviceId: Int
) : ViewModel() {
    private var _state = MutableStateFlow(ServiceUiState())
    val state = _state.asStateFlow()

    init {
        loadServiceInformation()
    }

    private fun loadServiceInformation() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val service = servicesRepository.getService(serviceId)
                _state.update { state ->
                    state.copy(
                        service = service,
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