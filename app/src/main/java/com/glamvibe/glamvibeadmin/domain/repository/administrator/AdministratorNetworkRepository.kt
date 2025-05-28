package com.glamvibe.glamvibeadmin.domain.repository.administrator

import com.glamvibe.glamvibeadmin.data.model.response.UserResponse
import com.glamvibe.glamvibeadmin.data.model.response.TokenPair
import com.glamvibe.glamvibeadmin.data.model.request.NewAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.UpdatedAdministrator

interface AdministratorNetworkRepository {
    suspend fun register(administrator: NewAdministrator): UserResponse
    suspend fun logIn(login: String, password: String): TokenPair
    suspend fun refreshTokenPair(refreshToken: String): TokenPair
    suspend fun logOut(id: Int)
    suspend fun getProfileInformation(refreshToken: String): UserResponse
    suspend fun updateProfileInformation(id: Int, newAdministrator: UpdatedAdministrator): UserResponse
}