package com.example.planradar.infrastructure.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PlanRadarApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("q") city: String, @Query("appid") apiKey: String): Response<WeatherResponseEntity>

    @GET("im/w/{iconId}.png")
    suspend fun getWeatherIcon(@Path("iconId") cityId:String): Response<String>
}