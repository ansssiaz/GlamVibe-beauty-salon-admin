package com.glamvibe.glamvibeclient.data.model.response

import com.glamvibe.glamvibeclient.domain.model.CurrentAppointment
import com.glamvibe.glamvibeclient.domain.model.Master
import com.glamvibe.glamvibeclient.domain.model.WorkingDay
import kotlinx.serialization.Serializable

@Serializable
data class MasterWithAppointments(
    val masterId: Int,
    val lastname: String,
    val name: String,
    val patronymic: String?,
    val schedule: List<WorkingDay> = emptyList(),
    val currentAppointments: List<CurrentAppointment> = emptyList()
)

fun MasterWithAppointments.toMaster(): Master = Master(
    id = masterId,
    lastname = lastname,
    name = name,
    patronymic = patronymic,
    schedule = schedule,
    appointments = currentAppointments
)