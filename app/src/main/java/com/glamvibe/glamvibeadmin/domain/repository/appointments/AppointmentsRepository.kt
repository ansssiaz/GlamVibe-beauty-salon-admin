package com.glamvibe.glamvibeadmin.domain.repository.appointments

import com.glamvibe.glamvibeadmin.data.model.request.CommentForCancellation
import com.glamvibe.glamvibeadmin.data.model.request.NewAppointment
import com.glamvibe.glamvibeadmin.domain.model.Appointment

interface AppointmentsRepository {
    suspend fun getAppointmentsForClient(clientId: Int): List<Appointment>
    suspend fun getAppointmentsForDate(year: Int, month: Int, day: Int): List<Appointment>
    suspend fun getAppointment(appointmentId: Int, clientId: Int): Appointment
    suspend fun makeAppointment(clientId: Int, newAppointment: NewAppointment): Appointment
    suspend fun confirmAppointment(appointmentId: Int): Appointment
    suspend fun completeAppointment(appointmentId: Int): Appointment
    suspend fun cancelAppointment(appointmentId: Int, comment: CommentForCancellation): Appointment
}