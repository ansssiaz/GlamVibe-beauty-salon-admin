package com.glamvibe.glamvibeadmin.domain.model

import com.glamvibe.glamvibeadmin.utils.Constants
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

enum class WeekDay {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

fun String.getWeekDayByString(): WeekDay {
    return when (this) {
        Constants.WeekDay.SUNDAY -> WeekDay.SUNDAY
        Constants.WeekDay.MONDAY -> WeekDay.MONDAY
        Constants.WeekDay.TUESDAY -> WeekDay.TUESDAY
        Constants.WeekDay.WEDNESDAY -> WeekDay.WEDNESDAY
        Constants.WeekDay.THURSDAY -> WeekDay.THURSDAY
        Constants.WeekDay.FRIDAY -> WeekDay.FRIDAY
        Constants.WeekDay.SATURDAY -> WeekDay.SATURDAY
        else -> WeekDay.MONDAY
    }
}


fun WeekDay.getStringByWeekDay(): String {
    return when (this) {
        WeekDay.SUNDAY -> Constants.WeekDay.SUNDAY
        WeekDay.MONDAY -> Constants.WeekDay.MONDAY
        WeekDay.TUESDAY -> Constants.WeekDay.TUESDAY
        WeekDay.WEDNESDAY -> Constants.WeekDay.WEDNESDAY
        WeekDay.THURSDAY -> Constants.WeekDay.THURSDAY
        WeekDay.FRIDAY -> Constants.WeekDay.FRIDAY
        WeekDay.SATURDAY -> Constants.WeekDay.SATURDAY
    }
}

fun DayOfWeek.toWeekDay(): WeekDay {
    return when (this) {
        DayOfWeek.MONDAY -> WeekDay.MONDAY
        DayOfWeek.TUESDAY -> WeekDay.TUESDAY
        DayOfWeek.WEDNESDAY -> WeekDay.WEDNESDAY
        DayOfWeek.THURSDAY -> WeekDay.THURSDAY
        DayOfWeek.FRIDAY -> WeekDay.FRIDAY
        DayOfWeek.SATURDAY -> WeekDay.SATURDAY
        DayOfWeek.SUNDAY -> WeekDay.SUNDAY
    }
}

@Serializable
data class WorkingDay(
    val weekDay: WeekDay,
    @Contextual val startTime: LocalTime,
    @Contextual val endTime: LocalTime,
)

@Serializable
data class Master(
    val id: Int = 0,
    val lastname: String = "",
    val name: String = "",
    val patronymic: String? = null,
    @Contextual val birthDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val photoUrl: String = "",
    val phone: String = "",
    val email: String = "",
    val specialty: String = "",
    val categories: List<String> = emptyList(),
    val schedule: List<WorkingDay> = emptyList(),
    @Contextual val dateOfEmployment: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val workExperience: Int = 0,
    val appointments: List<CurrentAppointment> = emptyList()
)

@Serializable
data class CurrentAppointment(
    @Contextual val date: LocalDate,
    @Contextual val time: LocalTime,
    val weekDay: WeekDay,
)


