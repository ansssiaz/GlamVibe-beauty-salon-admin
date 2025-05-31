package com.glamvibe.glamvibeadmin.data.repository.masters

import com.glamvibe.glamvibeadmin.data.api.MastersApi
import com.glamvibe.glamvibeadmin.data.model.response.toMaster
import com.glamvibe.glamvibeadmin.domain.model.Master
import com.glamvibe.glamvibeadmin.domain.model.WorkingDay
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class MastersRepositoryImpl(private val api: MastersApi) : MastersRepository {
    override suspend fun getMasters(): List<Master> = api.getMasters()
    override suspend fun getMaster(masterId: Int): Master = api.getMaster(masterId)
    override suspend fun getMasterWithCurrentAppointments(masterId: Int): Master =
        api.getMasterWithCurrentAppointments(masterId).toMaster()

    override suspend fun getMastersWithCurrentAppointments(): List<Master> =
        api.getMastersWithCurrentAppointments().map { it.toMaster() }

    override suspend fun addMaster(
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
    ): Master {
        val lastnamePart = lastname.toRequestBody("text/plain".toMediaType())
        val namePart = name.toRequestBody("text/plain".toMediaType())
        val patronymicPart = patronymic?.toRequestBody("text/plain".toMediaType())
        val birthDatePart = birthDate.toRequestBody("text/plain".toMediaType())
        val phonePart = phone.toRequestBody("text/plain".toMediaType())
        val emailPart = email.toRequestBody("text/plain".toMediaType())
        val specialtyPart = specialty.toRequestBody("text/plain".toMediaType())
        val categoriesPart =
            Json.encodeToString(categories).toRequestBody("text/plain".toMediaType())
        val schedulePart = Json.encodeToString(schedule).toRequestBody("text/plain".toMediaType())
        val dateOfEmploymentPart =
            dateOfEmployment.toRequestBody("text/plain".toMediaType())
        val workExperiencePart = workExperience.toRequestBody("text/plain".toMediaType())

        val fileRequestBody = photoBytes.toRequestBody("image/*".toMediaType(), 0, photoBytes.size)
        val photoPart = MultipartBody.Part.createFormData(
            "photo",
            "${UUID.randomUUID()}.${photoExtension}",
            fileRequestBody
        )

        return api.addMaster(
            lastnamePart,
            namePart,
            patronymicPart,
            birthDatePart,
            phonePart,
            emailPart,
            specialtyPart,
            categoriesPart,
            schedulePart,
            dateOfEmploymentPart,
            workExperiencePart,
            photoPart
        )
    }

    override suspend fun editMaster(
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
    ): Master {
        val lastnamePart = lastname.toRequestBody("text/plain".toMediaType())
        val namePart = name.toRequestBody("text/plain".toMediaType())
        val patronymicPart = patronymic?.toRequestBody("text/plain".toMediaType())
        val birthDatePart = birthDate.toRequestBody("text/plain".toMediaType())
        val phonePart = phone.toRequestBody("text/plain".toMediaType())
        val emailPart = email.toRequestBody("text/plain".toMediaType())
        val specialtyPart = specialty.toRequestBody("text/plain".toMediaType())
        val categoriesPart =
            Json.encodeToString(categories).toRequestBody("text/plain".toMediaType())
        val schedulePart = Json.encodeToString(schedule).toRequestBody("text/plain".toMediaType())
        val dateOfEmploymentPart =
            dateOfEmployment.toRequestBody("text/plain".toMediaType())
        val workExperiencePart = workExperience.toRequestBody("text/plain".toMediaType())

        if (photoBytes != null && photoExtension != null) {
            val fileRequestBody =
                photoBytes.toRequestBody("image/*".toMediaType(), 0, photoBytes.size)
            val photoPart = MultipartBody.Part.createFormData(
                "photo",
                "${UUID.randomUUID()}.${photoExtension}",
                fileRequestBody
            )
            return api.editMaster(
                id,
                lastnamePart,
                namePart,
                patronymicPart,
                birthDatePart,
                phonePart,
                emailPart,
                specialtyPart,
                categoriesPart,
                schedulePart,
                dateOfEmploymentPart,
                workExperiencePart,
                photoPart
            )
        } else {
            return api.editMaster(
                id,
                lastnamePart,
                namePart,
                patronymicPart,
                birthDatePart,
                phonePart,
                emailPart,
                specialtyPart,
                categoriesPart,
                schedulePart,
                dateOfEmploymentPart,
                workExperiencePart,
                null
            )
        }
    }

    override suspend fun deleteMaster(masterId: Int) = api.deleteMaster(masterId)
}