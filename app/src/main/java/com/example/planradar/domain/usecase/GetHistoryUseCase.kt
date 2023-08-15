package com.example.planradar.domain.usecase

import com.example.planradar.domain.outputport.PlanRadarRepository

class GetHistoryUseCase(private val repository: PlanRadarRepository) {
    suspend operator fun invoke(city: String) = repository.getHistory(city)
}