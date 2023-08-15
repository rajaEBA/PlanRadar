package com.example.planradar.domain.usecase

import com.example.planradar.domain.outputport.PlanRadarRepository

class GetCityWeatherIcon(private val repository: PlanRadarRepository) {
    suspend operator fun invoke(cityId: String) = repository.getWeatherIcon(cityId)
}