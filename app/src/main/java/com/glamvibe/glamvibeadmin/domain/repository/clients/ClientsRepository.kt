package com.glamvibe.glamvibeadmin.domain.repository.clients

import com.glamvibe.glamvibeadmin.data.model.response.UserResponse

interface ClientsRepository {
    suspend fun getClient(clientId: Int): UserResponse
    suspend fun getClients(): List<UserResponse>
}