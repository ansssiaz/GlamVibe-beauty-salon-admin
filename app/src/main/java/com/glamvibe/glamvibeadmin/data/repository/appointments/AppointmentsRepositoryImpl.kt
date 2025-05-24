package com.glamvibe.glamvibeadmin.data.repository.appointments

import com.glamvibe.glamvibeadmin.data.api.AppointmentsApi
import com.glamvibe.glamvibeadmin.data.model.request.CommentForCancellation
import com.glamvibe.glamvibeadmin.data.model.request.NewAppointment
import com.glamvibe.glamvibeadmin.domain.model.Appointment
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository

class AppointmentsRepositoryImpl(private val api: AppointmentsApi) : AppointmentsRepository {
    override suspend fun getAppointmentsForClient(clientId: Int): List<Appointment> =
        api.getAppointmentsForClient(clientId)

    override suspend fun getAppointmentsForDate(
        year: Int,
        month: Int,
        day: Int
    ): List<Appointment> = api.getAppointmentsForDate(year, month, day)

    override suspend fun getAppointment(appointmentId: Int, clientId: Int): Appointment =
        api.getAppointment(appointmentId, clientId)

    override suspend fun makeAppointment(
        clientId: Int,
        newAppointment: NewAppointment
    ): Appointment = api.makeAppointment(clientId, newAppointment)

    override suspend fun confirmAppointment(appointmentId: Int): Appointment = api.confirmAppointment(appointmentId)

    override suspend fun completeAppointment(appointmentId: Int): Appointment = api.completeAppointment(appointmentId)

    override suspend fun cancelAppointment(
        appointmentId: Int,
        comment: CommentForCancellation
    ): Appointment =
        api.cancelAppointment(
            appointmentId,
            comment
        )

}