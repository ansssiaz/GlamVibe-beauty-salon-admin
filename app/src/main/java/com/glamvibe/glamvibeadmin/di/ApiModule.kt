package com.glamvibe.glamvibeadmin.di

import com.glamvibe.glamvibeadmin.BuildConfig
import com.glamvibe.glamvibeadmin.data.api.AppointmentsApi
import com.glamvibe.glamvibeadmin.data.api.AuthInterceptor
import com.glamvibe.glamvibeadmin.data.api.AdministratorApi
import com.glamvibe.glamvibeadmin.data.api.MastersApi
import com.glamvibe.glamvibeadmin.data.api.PromotionsApi
import com.glamvibe.glamvibeadmin.data.api.ServicesApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single { AuthInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideAdministratorApi(get()) }
    single { provideServicesApi(get()) }
    single { provideMastersApi(get()) }
    single { provideAppointmentsApi(get()) }
    single { providePromotionsApi(get()) }
}

private val contentType = "application/json".toMediaType()
private val json = Json { ignoreUnknownKeys = true }

private fun provideOkHttpClient(authInterceptor: AuthInterceptor) = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    })
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
): Retrofit =
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://192.168.1.4:8080/")
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

private fun provideAdministratorApi(retrofit: Retrofit): AdministratorApi = retrofit.create(AdministratorApi::class.java)

private fun provideServicesApi(retrofit: Retrofit): ServicesApi =
    retrofit.create(ServicesApi::class.java)

private fun provideMastersApi(retrofit: Retrofit): MastersApi =
    retrofit.create(MastersApi::class.java)

private fun provideAppointmentsApi(retrofit: Retrofit): AppointmentsApi =
    retrofit.create(AppointmentsApi::class.java)

private fun providePromotionsApi(retrofit: Retrofit): PromotionsApi =
    retrofit.create(PromotionsApi::class.java)