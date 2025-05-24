package com.glamvibe.glamvibeadmin.domain.repository.administrator

import com.glamvibe.glamvibeadmin.data.model.response.TokenPair

interface AdministratorLocalRepository {
    fun checkTokenPair(): TokenPair
    fun saveTokenPair(accessToken: String, refreshToken: String)
    fun deleteTokenPair()
    fun isAccessTokenExpired(): Boolean
    fun getTokenExpirationTime(): Long
    fun saveTokenExpirationTime(expiresIn: Long)
}