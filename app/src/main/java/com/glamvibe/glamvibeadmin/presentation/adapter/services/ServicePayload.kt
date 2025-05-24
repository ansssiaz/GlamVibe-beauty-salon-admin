package com.glamvibe.glamvibeadmin.presentation.adapter.services

data class ServicePayload(
    val favourite: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = (favourite != null)
}