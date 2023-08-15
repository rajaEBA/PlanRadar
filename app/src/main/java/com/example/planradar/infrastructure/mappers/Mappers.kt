package com.example.planradar.infrastructure.mappers

import com.example.planradar.domain.model.*
import com.example.planradar.infrastructure.api.WeatherResponseEntity
import com.example.planradar.infrastructure.db.HistoryEntity
import com.example.planradar.infrastructure.utils.Utils

fun WeatherResponseEntity.toWeather(): WeatherResponse {
    return WeatherResponse(
        icon = this.weather[0].icon,
        description = this.weather[0].description,
        temp = this.main.temp,
        windSpeed = this.wind.speed,
        humidity = this.main.humidity,
        timezone = Utils.getCurrentDateTime(),
        name = this.name,
    )
}

fun WeatherResponse.toEntity(): HistoryEntity {
    return HistoryEntity(
        icon = this.icon,
        description = this.description,
        temp = this.temp,
        windSpeed = this.windSpeed,
        humidity = this.humidity,
        timezone = this.timezone,
        name = this.name
    )
}

fun HistoryEntity.toWeather(): WeatherResponse {
    return WeatherResponse(
        icon = this.icon,
        description = this.description,
        temp = this.temp,
        windSpeed = this.windSpeed,
        humidity = this.humidity,
        timezone = this.timezone,
        name = this.name,
    )
}