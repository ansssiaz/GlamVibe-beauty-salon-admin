package com.glamvibe.glamvibeadmin.domain.model

import com.glamvibe.glamvibeadmin.data.model.request.NewAdministrator
import com.glamvibe.glamvibeadmin.data.model.request.Role
import com.glamvibe.glamvibeadmin.data.model.request.UpdatedAdministrator
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

data class User(
    val id: Int = 0,
    val lastname: String = "",
    val name: String = "",
    val patronymic: String? = null,
    @Contextual val birthDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val email: String = "",
    val phone: String = "",
    val login: String = "",
    val password: String = "",
    val accessToken: String = "",
    val refreshToken: String? = null,
)

fun User.toNewAdministrator(formData: String?) = NewAdministrator(
    lastname = lastname,
    name = name,
    patronymic = patronymic,
    birthDate = birthDate,
    email = email,
    phone = phone,
    login = login,
    password = password,
    role = Role.ADMINISTRATOR,
    formData = formData,
)

fun User.toUpdatedAdministrator() = UpdatedAdministrator(
    lastname = lastname,
    name = name,
    patronymic = patronymic,
    birthDate = birthDate,
    email = email,
    phone = phone,
    login = login,
    password = password
)