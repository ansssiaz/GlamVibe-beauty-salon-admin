package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.data.model.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ClientsApi {
    @GET("clients")
    suspend fun getClients(): List<UserResponse>

    @GET("clients/{id}")
    suspend fun getClient(@Path("id") clientId: Int): UserResponse
}