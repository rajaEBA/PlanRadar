package com.example.planradar.domain.model

data class WeatherResponse(
    val icon: String,
    val description: String,
    val temp: Double,
    val windSpeed: Double,
    val humidity: Int,
    val timezone: String,
    val name: String,
)