package com.example.planradar

import android.app.Application
import com.example.planradar.di.PlanRadarDependencies
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class PlanRadarApplication : Application() {

    private val koinApp by lazy {
        startKoin {
            androidContext(this@PlanRadarApplication)
        }
    }

    override fun onCreate() {
        super.onCreate()
        koinApp.koin.loadModules(koinModuleList())
    }

    private fun koinModuleList() = ArrayList<Module>().apply {
        addAll(PlanRadarDependencies())
    }
}