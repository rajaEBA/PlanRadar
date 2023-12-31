package com.example.planradar.domain.usecase

import com.example.planradar.domain.outputport.PlanRadarRepository

class GetCityWeatherUseCase(private val repository: PlanRadarRepository) {
    suspend operator fun invoke(city: String) = repository.getWeather(city)
}