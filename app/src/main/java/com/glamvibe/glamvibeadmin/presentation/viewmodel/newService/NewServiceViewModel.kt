package com.glamvibe.glamvibeadmin.presentation.viewmodel.newService

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewServiceViewModel(
    private val servicesRepository: ServicesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewServiceUiState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val categories = servicesRepository.getCategories()
                _state.update { it.copy(categories = categories, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun addService(
        name: String,
        category: String,
        description: String,
        duration: String,
        price: String,
        imageBytes: ByteArray,
        imageExtension: String
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val newService = servicesRepository.addService(
                    name,
                    category,
                    description,
                    duration.toInt(),
                    price.toInt(),
                    imageBytes,
                    imageExtension
                )
                _state.update {
                    it.copy(
                        newService = newService,
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

    fun loadServiceToEdit(id: Int) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val service = servicesRepository.getService(id)
                _state.update { it.copy(serviceToEdit = service, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    /*fun editService() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val newService = servicesRepository.editService()
                _state.update { it.copy(newService = newService, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }*/
}