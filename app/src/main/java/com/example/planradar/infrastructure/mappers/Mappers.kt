package com.example.planradar.infrastructure.mappers

import com.example.planradar.domain.model.*
import com.example.planradar.infrastructure.api.WeatherResponseEntity
import com.example.planradar.infrastructure.db.HistoryEntity

fun WeatherResponseEntity.toWeather(): WeatherResponse {
    return WeatherResponse(
        coord = this.coord,
        weather = this.weather.map { weather -> Weather(weather.id,weather.main,weather.description,weather.icon) },
        base = this.base,
        main = Main(this.main.temp,this.main.feels_like,this.main.temp_min,this.main.temp_max,this.main.pressure,this.main.humidity),
        visibility = this.visibility,
        wind = Wind(this.wind.speed,this.wind.deg),
        clouds = Clouds(this.clouds.all),
        dt = this.dt,
        sys = Sys(this.sys.type,this.sys.id, this.sys.country,this.sys.sunrise,this.sys.sunset),
        timezone = this.timezone,
        id = this.id,
        name = this.name,
        cod = this.cod,
    )

}

fun HistoryEntity.toHistory(): History {
    return History(
        id = this.id,
        time = this.time,
        temp = this.temp,
        description = this.description
    )
}