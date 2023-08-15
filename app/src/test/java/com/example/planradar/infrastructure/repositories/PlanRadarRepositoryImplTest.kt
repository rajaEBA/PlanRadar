package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.infrastructure.api.Main
import com.example.planradar.infrastructure.api.Weather
import com.example.planradar.infrastructure.api.WeatherResponseEntity
import com.example.planradar.infrastructure.api.Wind
import com.example.planradar.infrastructure.mappers.toWeather
import com.example.planradar.infrastructure.utils.NetworkManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlanRadarRepositoryImplTest {

    private var localDataSource = mockk<WeatherLocalDataSource>()
    private var remoteDataSource = mockk<WeatherRemoteDataSource>()
    private var networkManager = mockk<NetworkManager>()

    private lateinit var repository: PlanRadarRepositoryImpl

    @Before
    fun setup() {
        repository =
            PlanRadarRepositoryImpl(remoteDataSource, localDataSource, networkManager)
    }

    @Test
    fun `should fetch weather with network connection`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns true
        val mapData = fakeWeatherResponseEntity.toWeather()
        coEvery { remoteDataSource.getWeatherOfCity("name") } returns fakeWeatherResponseEntity
        coEvery { localDataSource.addHistory(any()) } returns Unit

        //When
        val result = repository.getWeather("name")

        //Then
        assertEquals(Result.Success(mapData), result)
        coVerify(exactly = 1) { remoteDataSource.getWeatherOfCity("name") }
        coVerify(exactly = 1) { localDataSource.addHistory(mapData) }
    }

    @Test
    fun `should return no network connectivity error`() = runTest {
        //Given
        every { networkManager.hasConnection() } returns false
        coEvery { remoteDataSource.getWeatherOfCity("name") } returns fakeWeatherResponseEntity
        coEvery { localDataSource.addHistory(any()) } returns Unit

        //When
        val result = repository.getWeather("name")

        //Then
        Assert.assertTrue(result is Result.Error && result.exception.message == "No Network Connectivity")
    }

    @Test
    fun `should fetch history from cache`() = runTest {
        //Given
        coEvery { localDataSource.getAllHistories("name") } returns listOf(fakeData)

        //When
        val result = repository.getHistory("name")

        //Then
        assertEquals(Result.Success(listOf(fakeData)), result)
        coVerify(exactly = 1) { localDataSource.getAllHistories("name") }
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

    private val fakeWeatherResponseEntity = WeatherResponseEntity(
        weather = listOf(Weather(0, "main","description","icon")),
        main = Main(24.0,20.0,34.0,35.0,39,40,),
        wind = Wind(34.0,36),
        timezone = 38,
        id = 60,
        name = "city"
    )
}