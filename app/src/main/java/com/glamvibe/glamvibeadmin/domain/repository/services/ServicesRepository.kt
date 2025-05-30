package com.glamvibe.glamvibeadmin.domain.repository.services

import com.glamvibe.glamvibeadmin.domain.model.Service

interface ServicesRepository {
    suspend fun getServices(): List<Service>
    suspend fun getService(serviceId: Int): Service
    suspend fun getCategories(): List<String>
    suspend fun addService(
        name: String,
        category: String,
        description: String,
        duration: Int,
        price: Int,
        imageBytes: ByteArray,
        imageExtension: String
    ): Service

    suspend fun editService(
        id: Int,
        name: String,
        category: String,
        description: String,
        duration: Int,
        price: Int,
        imageBytes: ByteArray?,
        imageExtension: String?
    ): Service
}