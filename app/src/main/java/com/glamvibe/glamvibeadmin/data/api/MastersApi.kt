package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.data.model.response.MasterWithAppointments
import com.glamvibe.glamvibeadmin.domain.model.Master
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MastersApi {
    @GET("masters")
    suspend fun getMasters(): List<Master>

    @GET("masters/{id}")
    suspend fun getMaster(@Path("id") masterId: Int): Master

    @GET("masters/{masterId}/appointments")
    suspend fun getMasterWithCurrentAppointments(@Path("masterId") masterId: Int): MasterWithAppointments

    @GET("masters/appointments")
    suspend fun getMastersWithCurrentAppointments(): List<MasterWithAppointments>

    @Multipart
    @POST("masters")
    suspend fun addMaster(
        @Part("lastname") lastname: RequestBody,
        @Part("name") name: RequestBody,
        @Part("patronymic") patronymic: RequestBody?,
        @Part("birthDate") birthDate: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("specialty") specialty: RequestBody,
        @Part("categories") category: RequestBody,
        @Part("schedule") schedule: RequestBody,
        @Part("dateOfEmployment") dateOfEmployment: RequestBody,
        @Part("workExperience") workExperience: RequestBody,
        @Part photo: MultipartBody.Part
    ): Master

    @Multipart
    @PUT("masters/{id}")
    suspend fun editMaster(
        @Path("id") masterId: Int,
        @Part("lastname") lastname: RequestBody,
        @Part("name") name: RequestBody,
        @Part("patronymic") patronymic: RequestBody?,
        @Part("birthDate") birthDate: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("specialty") specialty: RequestBody,
        @Part("categories") category: RequestBody,
        @Part("schedule") schedule: RequestBody,
        @Part("dateOfEmployment") dateOfEmployment: RequestBody,
        @Part("workExperience") workExperience: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Master

    @DELETE("masters/{id}")
    suspend fun deleteMaster(@Path("id") masterId: Int)
}