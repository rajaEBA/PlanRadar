package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.model.History
import com.example.planradar.infrastructure.db.HistoryDao
import com.example.planradar.infrastructure.mappers.toHistory

class WeatherLocalDataSourceImpl(
    private val historyDao: HistoryDao
) : WeatherLocalDataSource {

    override suspend fun getAllHistories(): List<History> {
        val entities = historyDao.getHistory()
        return entities.map { entity -> entity.toHistory() }
    }
}

interface WeatherLocalDataSource {
    suspend fun getAllHistories(): List<History>
}