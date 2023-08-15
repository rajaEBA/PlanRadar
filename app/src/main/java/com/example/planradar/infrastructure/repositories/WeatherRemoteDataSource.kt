package com.example.planradar.infrastructure.repositories

import android.util.Log
import com.example.planradar.infrastructure.api.PlanRadarApi
import com.example.planradar.infrastructure.api.WeatherResponseEntity
import okio.IOException

class WeatherRemoteDataSourceImpl(
    private val service: PlanRadarApi
) : WeatherRemoteDataSource {

    override suspend fun getWeatherOfCity(city:String, appId: String): WeatherResponseEntity {
        return try {
            val response = service.getWeather(city,appId)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getWeatherIcon(cityId: String): String {
        return try {
            val response = service.getWeatherIcon(cityId)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                body
            } else {
                throw IOException(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }
}

interface WeatherRemoteDataSource {

    suspend fun getWeatherOfCity(city: String, appId: String): WeatherResponseEntity
    suspend fun getWeatherIcon(cityId:String) : String
}