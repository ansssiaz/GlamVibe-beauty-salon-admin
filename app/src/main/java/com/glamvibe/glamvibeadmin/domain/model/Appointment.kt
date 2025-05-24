package com.glamvibe.glamvibeadmin.domain.model

import com.glamvibe.glamvibeadmin.data.model.request.NewAppointmentDateTime
import com.glamvibe.glamvibeadmin.utils.Constants
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

enum class AppointmentStatus {
    IN_PROCESSING, WAITING, DONE, CANCELLATION_BY_THE_CLIENT, CANCELLATION_BY_THE_ADMINISTRATOR
}

fun String.getStatusByString(): AppointmentStatus {
    return when (this) {
        Constants.Status.IN_PROCESSING -> AppointmentStatus.IN_PROCESSING
        Constants.Status.WAITING -> AppointmentStatus.WAITING
        Constants.Status.DONE -> AppointmentStatus.DONE
        Constants.Status.CANCELLATION_BY_THE_CLIENT -> AppointmentStatus.CANCELLATION_BY_THE_CLIENT
        Constants.Status.CANCELLATION_BY_THE_ADMINISTRATOR -> AppointmentStatus.CANCELLATION_BY_THE_ADMINISTRATOR

        else -> AppointmentStatus.IN_PROCESSING
    }
}


fun AppointmentStatus.getStringByStatus(): String {
    return when (this) {
        AppointmentStatus.IN_PROCESSING -> Constants.Status.IN_PROCESSING
        AppointmentStatus.WAITING -> Constants.Status.WAITING
        AppointmentStatus.DONE -> Constants.Status.DONE
        AppointmentStatus.CANCELLATION_BY_THE_CLIENT -> Constants.Status.CANCELLATION_BY_THE_CLIENT
        AppointmentStatus.CANCELLATION_BY_THE_ADMINISTRATOR -> Constants.Status.CANCELLATION_BY_THE_ADMINISTRATOR
    }
}

@Serializable
data class Appointment(
    val id: Int = 0,
    val client: ClientData,
    val master: MasterData,
    val service: String = "",
    val promotionId: Int? = 0,
    @Contextual val date: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    @Contextual val time: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time,
    val weekDay: WeekDay = WeekDay.MONDAY,
    val status: AppointmentStatus = AppointmentStatus.IN_PROCESSING,
    val comment: String? = null,
)

@Serializable
data class ClientData(
    val id: Int = 0,
    val lastname: String = "",
    val name: String = "",
    val patronymic: String? = null,
)

@Serializable
data class MasterData(
    val id: Int = 0,
    val lastname: String = "",
    val name: String = "",
    val patronymic: String? = null,
)

fun Appointment.toNewAppointment() = NewAppointmentDateTime(
    date = date,
    time = time,
    weekDay = weekDay
)