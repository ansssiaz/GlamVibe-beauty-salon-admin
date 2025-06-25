package com.glamvibe.glamvibeadmin.presentation.viewmodel.clients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.response.toUser
import com.glamvibe.glamvibeadmin.domain.repository.clients.ClientsRepository

import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientsViewModel(
    private val clientsRepository: ClientsRepository
) : ViewModel() {
    private var _state = MutableStateFlow(ClientsUiState())
    val state = _state.asStateFlow()

    init {
        getClients()
    }

    private fun getClients() {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val clients = clientsRepository.getClients()
                    .map { it.toUser() }
                    .sortedBy { it.lastname }

                _state.update {
                    it.copy(
                        clients = clients,
                        foundClients = clients,
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

    fun searchClient(query: String) {
        val queryParts = query.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
            .map { it.lowercase() }

        _state.update { state ->
            val clients = state.clients?.filter { client ->
                val fields = listOfNotNull(
                    client.lastname.lowercase(),
                    client.name.lowercase(),
                    client.patronymic?.lowercase()
                )
                queryParts.all { part -> fields.any { field -> field.contains(part) } }
            }

            if (clients != null) {
                state.copy(
                    foundClients = clients
                )
            } else {
                state.copy(
                    foundClients = emptyList()
                )
            }
        }
    }

    fun resetSearch() {
        _state.update { state ->
            if (state.clients != null) {
                state.copy(
                    foundClients = state.clients
                )
            } else {
                state.copy(
                    foundClients = emptyList()
                )
            }
        }
    }
}