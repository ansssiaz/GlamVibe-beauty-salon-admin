package com.glamvibe.glamvibeadmin.data.repository.clients

import com.glamvibe.glamvibeadmin.data.api.ClientsApi
import com.glamvibe.glamvibeadmin.data.model.response.UserResponse
import com.glamvibe.glamvibeadmin.domain.repository.clients.ClientsRepository

class ClientsRepositoryImpl(private val api: ClientsApi) : ClientsRepository {
    override suspend fun getClient(clientId: Int): UserResponse = api.getClient(clientId)
    override suspend fun getClients(): List<UserResponse> = api.getClients()
}