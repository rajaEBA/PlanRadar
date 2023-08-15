package com.example.planradar.domain.outputport

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.History

interface PlanRadarRepository {

    suspend fun getWeather(city:String, appId:String): Result<WeatherResponse>

    suspend fun getHistory(): Result<List<History>>
}