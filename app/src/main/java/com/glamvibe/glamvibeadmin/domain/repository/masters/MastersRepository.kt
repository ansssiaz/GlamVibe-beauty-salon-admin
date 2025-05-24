package com.glamvibe.glamvibeadmin.domain.repository.masters

import com.glamvibe.glamvibeadmin.domain.model.Master

interface MastersRepository {
    suspend fun getMasters(): List<Master>
    suspend fun getMaster(masterId: Int): Master
    suspend fun getMasterWithCurrentAppointments(masterId: Int): Master
    suspend fun getMastersWithCurrentAppointments(): List<Master>
}