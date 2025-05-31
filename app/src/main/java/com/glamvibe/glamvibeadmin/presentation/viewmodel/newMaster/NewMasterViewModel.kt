package com.glamvibe.glamvibeadmin.presentation.viewmodel.newMaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewMasterViewModel(
    private val mastersRepository: MastersRepository,
    private val servicesRepository: ServicesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewMasterUiState())
    val state = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val categories = servicesRepository.getCategories()
                _state.update {
                    it.copy(
                        categories = categories,
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

    fun addMaster(
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        phone: String,
        email: String,
        specialty: String,
        categories: List<String>,
        schedule: List<WorkingDay>,
        dateOfEmployment: String,
        workExperience: String,
        photoBytes: ByteArray,
        photoExtension: String
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val newMaster = mastersRepository.addMaster(
                    lastname,
                    name,
                    patronymic,
                    birthDate,
                    phone,
                    email,
                    specialty,
                    categories,
                    schedule,
                    dateOfEmployment,
                    workExperience,
                    photoBytes,
                    photoExtension
                )
                _state.update {
                    it.copy(
                        newMaster = newMaster,
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

    fun loadMasterToEdit(id: Int) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val master = mastersRepository.getMaster(id)
                _state.update {
                    it.copy(
                        masterToEdit = master,
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

    fun editMaster(
        id: Int,
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        phone: String,
        email: String,
        specialty: String,
        categories: List<String>,
        schedule: List<WorkingDay>,
        dateOfEmployment: String,
        workExperience: String,
        photoBytes: ByteArray?,
        photoExtension: String?
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val newMaster = mastersRepository.editMaster(
                    id,
                    lastname,
                    name,
                    patronymic,
                    birthDate,
                    phone,
                    email,
                    specialty,
                    categories,
                    schedule,
                    dateOfEmployment,
                    workExperience,
                    photoBytes,
                    photoExtension
                )
                _state.update {
                    it.copy(
                        newMaster = newMaster,
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