package com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glamvibe.glamvibeadmin.data.model.response.toAdministrator
import com.glamvibe.glamvibeadmin.domain.model.Administrator
import com.glamvibe.glamvibeadmin.domain.model.toUpdatedAdministrator
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorLocalRepository
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorNetworkRepository
import com.glamvibe.glamvibeadmin.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class AdministratorViewModel(
    private val networkRepository: AdministratorNetworkRepository,
    private val localRepository: AdministratorLocalRepository
) : ViewModel() {
    private var _state = MutableStateFlow(AdministratorUiState())
    val state = _state.asStateFlow()

    fun checkTokenPair() {
        val tokenPair = localRepository.checkTokenPair()
        val administrator =
            if (tokenPair.accessToken.isNotEmpty() && tokenPair.refreshToken.isNotEmpty())
                Administrator(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                ) else null
        localRepository.saveTokenPair(tokenPair.accessToken, tokenPair.refreshToken)
        _state.update { it.copy(administrator = administrator, status = Status.Idle) }
    }

    fun logIn(login: String, password: String) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val tokenPair = networkRepository.logIn(login, password)
                val administrator = Administrator(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                )
                localRepository.saveTokenPair(tokenPair.accessToken, tokenPair.refreshToken)
                localRepository.saveTokenExpirationTime(1800)
                _state.update { it.copy(administrator = administrator, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun logOut(id: Int) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                networkRepository.logOut(id)
                localRepository.deleteTokenPair()
                _state.update { it.copy(administrator = null, status = Status.Idle) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(status = Status.Error(e))
                }
            }
        }
    }

    fun getProfileInformation() {
        val refreshToken = localRepository.checkTokenPair().refreshToken

        if (refreshToken.isNotEmpty()) {
            _state.update { it.copy(status = Status.Loading) }

            viewModelScope.launch {
                try {
                    val administrator =
                        networkRepository.getProfileInformation(refreshToken).toAdministrator()
                    _state.update { it.copy(administrator = administrator, status = Status.Idle) }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(status = Status.Error(e))
                    }
                }
            }
        }
    }

    fun updateProfileInformation(
        id: Int,
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        email: String,
        phone: String,
        login: String,
        password: String
    ) {
        _state.update { it.copy(status = Status.Loading) }

        viewModelScope.launch {
            try {
                val administrator = Administrator(
                    lastname = lastname,
                    name = name,
                    patronymic = patronymic,
                    birthDate = LocalDate.parse(birthDate),
                    email = email,
                    phone = phone,
                    login = login,
                    password = password,
                )
                val updatedAdministrator = networkRepository.updateProfileInformation(
                    id, administrator.toUpdatedAdministrator()
                ).toAdministrator()

                _state.update {
                    it.copy(
                        administrator = updatedAdministrator,
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