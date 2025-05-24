package com.glamvibe.glamvibeadmin.utils

import kotlinx.datetime.LocalTime

fun LocalTime.plusMinutes(minutes: Int): LocalTime {
    val totalMinutes = this.hour * 60 + this.minute + minutes
    val newHour = (totalMinutes / 60) % 24
    val newMinute = totalMinutes % 60
    return LocalTime(newHour, newMinute, this.second, this.nanosecond)
}