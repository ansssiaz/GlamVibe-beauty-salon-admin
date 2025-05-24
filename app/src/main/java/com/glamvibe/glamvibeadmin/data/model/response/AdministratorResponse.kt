package com.glamvibe.glamvibeadmin.data.model.response

import com.glamvibe.glamvibeadmin.domain.model.Administrator
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class AdministratorResponse(
    val id: Int = 0,
    val lastname: String = "",
    val name: String = "",
    val patronymic: String? = null,
    @Contextual val birthDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val email: String = "",
    val phone: String = "",
    val login: String = "",
    val passwordHash: String = "",
    val refreshToken: String? = null,
    val refreshTokenExpirationTime: Long? = null,
    val role: String = "CLIENT",
    val favouritesIds: List<Int?> = emptyList<Int>(),
    var form: Form? = null,
)

@Serializable
data class Form(
    val id: Int = 0,
    val clientData: String = "",
)

@Serializable
data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)

fun AdministratorResponse.toAdministrator() = Administrator(
    id = id,
    lastname = lastname,
    name = name,
    patronymic = patronymic,
    birthDate = birthDate,
    email = email,
    phone = phone,
    login = login,
    refreshToken = refreshToken,
)