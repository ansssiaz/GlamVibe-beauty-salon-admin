package com.glamvibe.glamvibeadmin.domain.repository.services

import com.glamvibe.glamvibeadmin.domain.model.Service

interface ServicesRepository {
    suspend fun getServices(): List<Service>
    suspend fun getService(serviceId: Int): Service
}