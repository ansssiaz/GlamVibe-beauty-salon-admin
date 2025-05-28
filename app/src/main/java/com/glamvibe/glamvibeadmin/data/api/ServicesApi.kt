package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.domain.model.Service
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicesApi {
    @GET("services")
    suspend fun getServices(): List<Service>

    @GET("services/{id}")
    suspend fun getService(@Path("id") serviceId: Int): Service
}