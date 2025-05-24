package com.glamvibe.glamvibeadmin

import android.app.Application
import com.glamvibe.glamvibeadmin.di.apiModule
import com.glamvibe.glamvibeadmin.di.repositoryModule
import com.glamvibe.glamvibeadmin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(listOf(repositoryModule, apiModule, viewModelModule))
        }
    }
}