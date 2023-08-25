package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.outputport.PlanRadarRepository
import com.example.planradar.infrastructure.utils.NetworkManager
import com.example.planradar.domain.common.Result
import com.example.planradar.infrastructure.mappers.toWeather

class PlanRadarRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val networkManager: NetworkManager,
) : PlanRadarRepository {

    override suspend fun getWeather(city: String): Result<WeatherResponse> {
        return if (networkManager.hasConnection()) {
            try {
                val remoteWeather = remoteDataSource.getWeatherOfCity(city)
                val weather = remoteWeather.toWeather()
                insertToDatabase(weather)

                Result.Success(weather)
            } catch (e: Exception) {
                Result.Error(Exception("No valid data"))
            }

        } else {
            Result.Error(Exception("No Network Connectivity"))
        }
    }

    override suspend fun getHistory(city:String): Result<List<WeatherResponse>> {
        return try {
            Result.Success(localDataSource.getAllHistories(city))
        } catch (e: Exception) {
            Result.Success(emptyList())
        }
    }

    private suspend fun insertToDatabase(item: WeatherResponse) {
        localDataSource.addHistory(item)
    }

}