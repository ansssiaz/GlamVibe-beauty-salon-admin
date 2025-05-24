package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.data.model.response.AdministratorResponse
import com.glamvibe.glamvibeadmin.data.model.response.TokenPair
import com.glamvibe.glamvibeadmin.data.model.request.LogInAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.NewAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.RefreshToken
import com.glamvibe.glamvibeadmin.data.model.request.UpdatedAdministrator
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdministratorApi {
    @POST("auth/registration")
    suspend fun register(@Body newAdministrator: NewAdministrator): AdministratorResponse

    @POST("auth/login")
    suspend fun logIn(@Body logInAdministrator: LogInAdministrator): TokenPair

    @POST("auth/token-refresh")
    suspend fun refreshTokenPair(@Body refreshToken: RefreshToken): TokenPair

    @POST("auth/logout/{id}")
    suspend fun logOut(@Path("id") id: Int)

    @POST("me")
    suspend fun getProfileInformation(@Body refreshToken: RefreshToken): AdministratorResponse

    @PUT("me/{id}")
    suspend fun updateProfileInformation(@Path("id") id: Int, @Body newClient: UpdatedAdministrator): AdministratorResponse
}