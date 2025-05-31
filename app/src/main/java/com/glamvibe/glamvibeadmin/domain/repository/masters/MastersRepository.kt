package com.glamvibe.glamvibeadmin.domain.repository.masters

import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay

interface MastersRepository {
    suspend fun getMasters(): List<Master>
    suspend fun getMaster(masterId: Int): Master
    suspend fun getMasterWithCurrentAppointments(masterId: Int): Master
    suspend fun getMastersWithCurrentAppointments(): List<Master>

    suspend fun addMaster(
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        phone: String,
        email: String,
        specialty: String,
        categories: List<String>,
        schedule: List<WorkingDay>,
        dateOfEmployment: String,
        workExperience: String,
        photoBytes: ByteArray,
        photoExtension: String
    ): Master

    suspend fun editMaster(
        id: Int,
        lastname: String,
        name: String,
        patronymic: String?,
        birthDate: String,
        phone: String,
        email: String,
        specialty: String,
        categories: List<String>,
        schedule: List<WorkingDay>,
        dateOfEmployment: String,
        workExperience: String,
        photoBytes: ByteArray?,
        photoExtension: String?
    ): Master

    suspend fun deleteMaster(masterId: Int)
}