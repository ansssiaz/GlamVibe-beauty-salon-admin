package com.glamvibe.glamvibeadmin.presentation.viewmodel.newPromotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.promotions.PromotionsRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPromotionViewModel(
    private val promotionsRepository: PromotionsRepository,
    //private val servicesRepository: ServicesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewPromotionUiState())
    val state = _state.asStateFlow()

/*    init {
        loadServices()
    }

    private fun loadServices() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val services = servicesRepository.getServices().map { it.name }
                _state.update { it.copy(services = services, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }*/

    /*    fun addPromotion(
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
    }*/

    fun loadPromotionToEdit(id: Int) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val promotion = promotionsRepository.getPromotion(id)
                _state.update { it.copy(promotionToEdit = promotion, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    /* fun editService(
        id: Int,
        name: String,
        category: String,
        description: String,
        duration: String,
        price: String,
        imageBytes: ByteArray?,
        imageExtension: String?
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val newService = servicesRepository.editService(
                    id,
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
    }*/
}