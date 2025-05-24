package com.glamvibe.glamvibeadmin.data.api

import com.glamvibe.glamvibeadmin.data.model.request.CommentForCancellation
import com.glamvibe.glamvibeadmin.data.model.request.NewAppointment
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentsApi {
    @GET("appointments/{year}/{month}/{day}")
    suspend fun getAppointmentsForDate(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int
    ): List<Appointment>

    @GET("appointments/{clientId}")
    suspend fun getAppointmentsForClient(@Path("clientId") clientId: Int): List<Appointment>

    @GET("appointments/{appointmentId}/clients/{clientId}")
    suspend fun getAppointment(
        @Path("appointmentId") appointmentId: Int,
        @Path("clientId") clientId: Int
    ): Appointment

    @POST("appointments/{clientId}")
    suspend fun makeAppointment(
        @Path("clientId") clientId: Int,
        @Body newAppointment: NewAppointment
    ): Appointment

    @POST("appointments/confirm/{appointmentId}")
    suspend fun confirmAppointment(
        @Path("appointmentId") appointmentId: Int,
    ): Appointment

    @POST("appointments/complete/{appointmentId}")
    suspend fun completeAppointment(
        @Path("appointmentId") appointmentId: Int,
    ): Appointment

    @POST("appointments/admin/cancel/{appointmentId}")
    suspend fun cancelAppointment(
        @Path("appointmentId") appointmentId: Int,
        @Body comment: CommentForCancellation,
    ): Appointment
}