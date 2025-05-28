package com.glamvibe.glamvibeadmin.presentation.viewmodel.newAdministrator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.response.toUser
import com.glamvibe.glamvibeadmin.domain.model.User
import com.glamvibe.glamvibeadmin.domain.model.toNewAdministrator
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorNetworkRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class NewAdministratorViewModel(
    private val networkRepository: AdministratorNetworkRepository
) : ViewModel() {
    private var _state = MutableStateFlow(NewAdministratorUiState())
    val state = _state.asStateFlow()

    fun register(
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        email: String,
        phone: String,
        login: String,
        password: String,
        formData: String?
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val administrator = User(
                    lastname = lastname,
                    name = name,
                    patronymic = patronymic,
                    birthDate = LocalDate.parse(birthDate),
                    email = email,
                    phone = phone,
                    login = login,
                    password = password,
                )

                val newAdministrator = administrator.toNewAdministrator(formData)
                val registeredAdministrator =
                    networkRepository.register(newAdministrator).toUser()

                _state.update {
                    it.copy(
                        administrator = registeredAdministrator,
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