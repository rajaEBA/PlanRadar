package com.example.planradar.di

import com.example.planradar.domain.outputport.PlanRadarRepository
import com.example.planradar.domain.usecase.GetCityWeatherUseCase
import com.example.planradar.domain.usecase.GetHistoryUseCase
import com.example.planradar.infrastructure.api.ApiExecutor
import com.example.planradar.infrastructure.api.ApiExecutor.PLANRADAR_APIS_ENDPOINT
import com.example.planradar.infrastructure.db.HistoryDatabase
import com.example.planradar.infrastructure.repositories.PlanRadarRepositoryImpl
import com.example.planradar.infrastructure.repositories.WeatherLocalDataSourceImpl
import com.example.planradar.infrastructure.repositories.WeatherRemoteDataSource
import com.example.planradar.infrastructure.repositories.WeatherRemoteDataSourceImpl
import com.example.planradar.infrastructure.utils.NetworkManager
import com.example.planradar.presentation.ui.city.CityViewModel
import com.example.planradar.presentation.ui.details.DetailsViewModel
import com.example.planradar.presentation.ui.history.HistoryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun planRadarDependencies(): List<Module> {
    return listOf(weatherModule())
}

private fun weatherModule() = module {

    val httpClient = ApiExecutor.createApi(PLANRADAR_APIS_ENDPOINT)
    single<WeatherRemoteDataSource> {
        WeatherRemoteDataSourceImpl(
            service = httpClient,
        )
    }

    single { NetworkManager(androidApplication()) }

    single<PlanRadarRepository>(createdAtStart = true) {

        val database = HistoryDatabase.getDatabase(androidApplication())

        val localDataSource = WeatherLocalDataSourceImpl(
            historyDao = database.historyDao()
        )

        PlanRadarRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = localDataSource,
            networkManager = get(),
        )
    }

    factory {
        GetCityWeatherUseCase(
            repository = get()
        )
    }

    factory {
        GetHistoryUseCase(
            repository = get()
        )
    }

    viewModel {
        CityViewModel()
    }

    viewModel {
        DetailsViewModel(
            weather = get(),
        )
    }

    viewModel {
        HistoryViewModel(
            history = get(),
        )
    }
}
