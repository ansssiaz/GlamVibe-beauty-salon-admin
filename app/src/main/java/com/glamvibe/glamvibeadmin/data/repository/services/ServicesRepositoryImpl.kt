package com.glamvibe.glamvibeadmin.data.repository.services

import com.glamvibe.glamvibeadmin.data.api.ServicesApi
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository

class ServicesRepositoryImpl(private val api: ServicesApi) : ServicesRepository {
    override suspend fun getServices(clientId: Int): List<Service> = api.getServices(clientId)
    override suspend fun getService(serviceId: Int, clientId: Int): Service =
        api.getService(serviceId, clientId)
}