package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.domain.model.Service
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ServicesApi {
    @GET("services")
    suspend fun getServices(): List<Service>

    @GET("services/{id}")
    suspend fun getService(@Path("id") serviceId: Int): Service

    @GET("services/categories")
    suspend fun getCategories(): List<String>

    @Multipart
    @POST("services")
    suspend fun addService(
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("duration") duration: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part
    ): Service

    @Multipart
    @PUT("services/{id}")
    suspend fun editService(
        @Path("id") serviceId: Int,
        @Part("name") name: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("duration") duration: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): Service
}