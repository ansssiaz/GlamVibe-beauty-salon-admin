package com.glamvibe.glamvibeadmin.data.model.request

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    ADMINISTRATOR
}

@Serializable
data class LogInAdministrator(
    val login: String,
    val password: String,
)

@Serializable
data class RefreshToken(
    val refreshToken: String,
)

@Serializable
data class NewAdministrator(
    val lastname: String,
    val name: String,
    val patronymic: String?,
    @Contextual val birthDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val phone: String,
    val email: String,
    val login: String,
    val password: String,
    val role: Role,
    val formData: String?,
)

@Serializable
data class UpdatedAdministrator(
    val lastname: String,
    val name: String,
    val patronymic: String?,
    @Contextual val birthDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val email: String,
    val phone: String,
    val login: String,
    val password: String?,
)