package com.glamvibe.glamvibeadmin.data.repository.services

import com.glamvibe.glamvibeadmin.data.api.ServicesApi
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository

class ServicesRepositoryImpl(private val api: ServicesApi) : ServicesRepository {
    override suspend fun getServices(): List<Service> = api.getServices()
    override suspend fun getService(serviceId: Int): Service = api.getService(serviceId)
}