package com.example.planradar.domain.usecase

import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.outputport.PlanRadarRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import com.example.planradar.domain.common.Result
import org.junit.Assert.assertTrue

class GetCityWeatherUseCaseTest {

    private lateinit var getCityWeatherUseCase: GetCityWeatherUseCase
    private val repository = mockk<PlanRadarRepository>(relaxed = true)

    @Before
    fun setUp() {
        getCityWeatherUseCase = GetCityWeatherUseCase(repository)
    }

    @Test
    fun `should fetch data successfully`() = runTest {
        //Given
        coEvery { repository.getWeather("city") } returns Result.Success(fakeData)

        //When
        val result = getCityWeatherUseCase.invoke("city")

        //Then
        assertEquals(result, Result.Success(fakeData))
    }

    @Test
    fun `should handle error case`() = runTest {
        //Given
        coEvery { repository.getWeather("city") } returns Result.Error(Exception("an error occurred."))

        //When
        val result = getCityWeatherUseCase.invoke("city")

        //Then
        assertTrue(result is Result.Error)
        assertEquals("an error occurred.", (result as Result.Error).exception.message)
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
}