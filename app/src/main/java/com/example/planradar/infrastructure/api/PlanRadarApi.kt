package com.example.planradar.infrastructure.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanRadarApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String): Response<WeatherResponseEntity>
}