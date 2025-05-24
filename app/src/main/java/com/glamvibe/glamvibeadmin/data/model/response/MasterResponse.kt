package com.glamvibe.glamvibeadmin.data.model.response

import com.glamvibe.glamvibeadmin.domain.model.CurrentAppointment
import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import kotlinx.serialization.Serializable

@Serializable
data class MasterWithAppointments(
    val masterId: Int,
    val lastname: String,
    val name: String,
    val patronymic: String?,
    val categories: List<String> = emptyList(),
    val schedule: List<WorkingDay> = emptyList(),
    val currentAppointments: List<CurrentAppointment> = emptyList()
)

fun MasterWithAppointments.toMaster(): Master = Master(
    id = masterId,
    lastname = lastname,
    name = name,
    patronymic = patronymic,
    schedule = schedule,
    categories = categories,
    appointments = currentAppointments
)