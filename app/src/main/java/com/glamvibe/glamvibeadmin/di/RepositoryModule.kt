package com.glamvibe.glamvibeadmin.di

import com.glamvibe.glamvibeadmin.data.repository.appointments.AppointmentsRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.administrator.AdministratorLocalRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.administrator.AdministratorNetworkRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.clients.ClientsRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.masters.MastersRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.promotions.PromotionRepositoryImpl
import com.glamvibe.glamvibeadmin.data.repository.services.ServicesRepositoryImpl
import com.glamvibe.glamvibeadmin.domain.repository.appointments.AppointmentsRepository
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorLocalRepository
import com.glamvibe.glamvibeadmin.domain.repository.administrator.AdministratorNetworkRepository
import com.glamvibe.glamvibeadmin.domain.repository.clients.ClientsRepository
import com.glamvibe.glamvibeadmin.domain.repository.masters.MastersRepository
import com.glamvibe.glamvibeadmin.domain.repository.promotions.PromotionsRepository
import com.glamvibe.glamvibeadmin.domain.repository.services.ServicesRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { provideAdministratorNetworkRepository(get()) }
    single { provideAdministratorLocalRepository(get()) }
    single { provideServicesRepository(get()) }
    single { provideMastersRepository(get()) }
    single { provideAppointmentsRepository(get()) }
    single { providePromotionsRepository(get()) }
    single { provideClientsRepository(get()) }

    single<AdministratorNetworkRepository> {
        return@single AdministratorNetworkRepositoryImpl(get())
    }

    single<AdministratorLocalRepository> {
        return@single AdministratorLocalRepositoryImpl(get())
    }

    single<ServicesRepository> {
        return@single ServicesRepositoryImpl(get())
    }

    single<MastersRepository> {
        return@single MastersRepositoryImpl(get())
    }

    single<AppointmentsRepository> {
        return@single AppointmentsRepositoryImpl(get())
    }

    single<PromotionsRepository> {
        return@single PromotionRepositoryImpl(get())
    }

    single<ClientsRepository> {
        return@single ClientsRepositoryImpl(get())
    }
}

private fun provideAdministratorNetworkRepository(repository: AdministratorNetworkRepositoryImpl): AdministratorNetworkRepository =
    repository

private fun provideAdministratorLocalRepository(repository: AdministratorLocalRepositoryImpl): AdministratorLocalRepository =
    repository

private fun provideServicesRepository(repository: ServicesRepositoryImpl): ServicesRepository =
    repository

private fun provideMastersRepository(repository: MastersRepositoryImpl): MastersRepository =
    repository

private fun provideAppointmentsRepository(repository: AppointmentsRepositoryImpl): AppointmentsRepository =
    repository

private fun providePromotionsRepository(repository: PromotionRepositoryImpl): PromotionsRepository =
    repository

private fun provideClientsRepository(repository: ClientsRepositoryImpl): ClientsRepository =
    repository