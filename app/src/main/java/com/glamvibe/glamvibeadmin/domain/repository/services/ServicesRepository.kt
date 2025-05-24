package com.glamvibe.glamvibeadmin.domain.repository.services

import com.glamvibe.glamvibeadmin.domain.model.Service

interface ServicesRepository {
    suspend fun getServices(clientId: Int): List<Service>
    suspend fun getService(serviceId: Int, clientId: Int): Service
}