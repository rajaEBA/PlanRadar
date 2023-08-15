package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.infrastructure.db.HistoryDao
import com.example.planradar.infrastructure.db.HistoryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherLocalDataSourceImplTest {

    private var historyDao = mockk<HistoryDao>(relaxed = true)
    private lateinit var weatherLocalDataSource: WeatherLocalDataSource

    @Before
    fun setup() {
        weatherLocalDataSource = WeatherLocalDataSourceImpl(historyDao)
    }

    @Test
    fun `should add history successfully`() = runTest {
        // When
        weatherLocalDataSource.addHistory(fakeData)

        // Then
        coVerify { historyDao.insert(fakeHistoryData) }
    }

    @Test
    fun `should get all history successfully`() = runTest {
        // Given
        coEvery { historyDao.getHistory(any()) } returns listOf(fakeHistoryData)

        // When
        val result = weatherLocalDataSource.getAllHistories("city")

        // Then
        assertEquals(listOf(fakeData), result)
    }

    private val fakeData = WeatherResponse(
        icon = "icon",
        description = "description",
        temp = 27.0,
        windSpeed = 45.0,
        humidity = 35,
        timezone = "time",
        name = "name"
    )

    private val fakeHistoryData = HistoryEntity(
        id = 0,
        icon = "icon",
        description = "description",
        temp = 27.0,
        windSpeed = 45.0,
        humidity = 35,
        timezone = "time",
        name = "name"
    )
}