package com.glamvibe.glamvibeadmin.di

import com.glamvibe.glamvibeadmin.presentation.viewmodel.clientAppointments.ClientAppointmentsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.administrator.AdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.appointments.AppointmentsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.master.MasterViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.masters.MastersViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newAppointment.NewAppointmentViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newAdministrator.NewAdministratorViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newMaster.NewMasterViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newService.NewServiceViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.promotion.PromotionViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.promotions.PromotionsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.service.ServiceViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.clients.ClientsViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.newPromotion.NewPromotionViewModel
import com.glamvibe.glamvibeadmin.presentation.viewmodel.services.ServicesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AdministratorViewModel(get(), get())
    }

    viewModel {
        NewAdministratorViewModel(get())
    }

    viewModel {
        ServicesViewModel(get())
    }

    viewModel { (serviceId: Int) ->
        ServiceViewModel(get(), serviceId)
    }

    viewModel {
        MastersViewModel(get())
    }

    viewModel { (clientId: Int) ->
        ClientAppointmentsViewModel(get(), clientId)
    }

    viewModel {
        NewAppointmentViewModel(get(), get(), get(), get())
    }

    viewModel {
        PromotionsViewModel(get())
    }

    viewModel { (clientId: Int) ->
        PromotionViewModel(get(), clientId)
    }

    viewModel {
        AppointmentsViewModel(get())
    }

    viewModel {
        NewServiceViewModel(get())
    }

    viewModel { (masterId: Int) ->
        MasterViewModel(get(), masterId)
    }

    viewModel {
        NewMasterViewModel(get(), get())
    }

    viewModel {
        ClientsViewModel(get())
    }

    viewModel {
        NewPromotionViewModel(get())
    }
}