package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.outputport.PlanRadarRepository
import com.example.planradar.infrastructure.utils.NetworkManager
import com.example.planradar.domain.common.Result
import com.example.planradar.infrastructure.mappers.toWeather

class PlanRadarRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val networkManager: NetworkManager,
) : PlanRadarRepository {

    override suspend fun getWeather(city:String, appId:String): Result<WeatherResponse> {
        return if (networkManager.hasConnection()) {
            val remoteWeather = remoteDataSource.getWeatherOfCity(city,appId)
            val weather = remoteWeather.toWeather()
            Result.Success(weather)

        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

    override suspend fun getWeatherIcon(cityId: String): Result<String> {
        return if (networkManager.hasConnection()) {
            val icon = remoteDataSource.getWeatherIcon(cityId)
            Result.Success(icon)
        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }
}