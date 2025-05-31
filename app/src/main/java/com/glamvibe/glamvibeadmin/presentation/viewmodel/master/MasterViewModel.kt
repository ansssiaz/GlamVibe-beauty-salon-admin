package com.glamvibe.glamvibeadmin.presentation.viewmodel.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MasterViewModel(
    private val mastersRepository: MastersRepository,
    private val masterId: Int
) : ViewModel() {
    private var _state = MutableStateFlow(MasterUiState())
    val state = _state.asStateFlow()

    init {
        loadMasterInformation()
    }

    private fun loadMasterInformation() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val master = mastersRepository.getMaster(masterId)
                _state.update { state ->
                    state.copy(
                        master = master,
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