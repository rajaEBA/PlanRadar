package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.infrastructure.db.HistoryDao
import com.example.planradar.infrastructure.mappers.toEntity
import com.example.planradar.infrastructure.mappers.toWeather

class WeatherLocalDataSourceImpl(
    private val historyDao: HistoryDao
) : WeatherLocalDataSource {

    override suspend fun getAllHistories(city:String): List<WeatherResponse> {
        val entities = historyDao.getHistory(city)
        return entities.map { entity -> entity.toWeather() }
    }

    override suspend fun addHistory(item: WeatherResponse) {
        historyDao.insert(item.toEntity())
    }
}

interface WeatherLocalDataSource {
    suspend fun getAllHistories(city:String): List<WeatherResponse>
    suspend fun addHistory(item:WeatherResponse)
}