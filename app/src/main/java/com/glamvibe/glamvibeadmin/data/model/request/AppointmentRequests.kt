package com.glamvibe.glamvibeadmin.data.model.request

import com.glamvibe.glamvibeadmin.domain.model.WeekDay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class NewAppointment(
    val clientId: Int = 0,
    val serviceId: Int = 0,
    val masterId: Int = 0,
    @Contextual val date: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    @Contextual val time: LocalTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).time,
    val weekDay: WeekDay = WeekDay.MONDAY,
)

@Serializable
data class NewAppointmentDateTime(
    @Contextual val date: LocalDate,
    @Contextual val time: LocalTime,
    val weekDay: WeekDay,
)

@Serializable
data class CommentForCancellation(
    val text: String,
)