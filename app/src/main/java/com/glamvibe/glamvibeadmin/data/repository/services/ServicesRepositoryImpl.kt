package com.glamvibe.glamvibeadmin.data.repository.services

import com.glamvibe.glamvibeadmin.data.api.ServicesApi
import com.glamvibe.glamvibeadmin.domain.model.Service
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class ServicesRepositoryImpl(private val api: ServicesApi) : ServicesRepository {
    override suspend fun getServices(): List<Service> = api.getServices()

    override suspend fun getService(serviceId: Int): Service = api.getService(serviceId)

    override suspend fun getCategories(): List<String> = api.getCategories()

    override suspend fun addService(
        name: String,
        category: String,
        description: String,
        duration: Int,
        price: Int,
        imageBytes: ByteArray,
        imageExtension: String
    ): Service {
        val namePart = name.toRequestBody("text/plain".toMediaType())
        val categoryPart = category.toRequestBody("text/plain".toMediaType())
        val descriptionPart = description.toRequestBody("text/plain".toMediaType())
        val durationPart = duration.toString().toRequestBody("text/plain".toMediaType())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaType())

        val fileRequestBody = imageBytes.toRequestBody("image/*".toMediaType(), 0, imageBytes.size)
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            "${UUID.randomUUID()}.${imageExtension}",
            fileRequestBody
        )

        return api.addService(
            namePart,
            categoryPart,
            descriptionPart,
            durationPart,
            pricePart,
            imagePart
        )
    }
}