package com.glamvibe.glamvibeadmin.data.repository.administrator

import com.glamvibe.glamvibeadmin.data.api.AdministratorApi
import com.glamvibe.glamvibeadmin.data.model.response.AdministratorResponse
import com.glamvibe.glamvibeadmin.data.model.request.RefreshToken
import com.glamvibe.glamvibeadmin.data.model.response.TokenPair
import com.glamvibe.glamvibeadmin.data.model.request.LogInAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.NewAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.UpdatedAdministrator
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorNetworkRepository

class AdministratorNetworkRepositoryImpl(private val api: AdministratorApi) :
    AdministratorNetworkRepository {
    override suspend fun register(administrator: NewAdministrator): AdministratorResponse =
        api.register(administrator)

    override suspend fun logIn(login: String, password: String): TokenPair {
        val logInAdministrator = LogInAdministrator(login, password)
        return api.logIn(logInAdministrator)
    }

    override suspend fun refreshTokenPair(refreshToken: String): TokenPair {
        val token = RefreshToken(refreshToken)
        return api.refreshTokenPair(token)
    }

    override suspend fun logOut(id: Int) = api.logOut(id)

    override suspend fun getProfileInformation(refreshToken: String): AdministratorResponse {
        val token = RefreshToken(refreshToken)
        return api.getProfileInformation(token)
    }

    override suspend fun updateProfileInformation(
        id: Int,
        newAdministrator: UpdatedAdministrator
    ): AdministratorResponse =
        api.updateProfileInformation(id, newAdministrator)
}