package com.example.planradar.domain.usecase

import com.example.planradar.domain.common.Result
import com.example.planradar.domain.outputport.PlanRadarRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import com.example.planradar.domain.model.WeatherResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetHistoryUseCaseTest {

    private lateinit var getHistoryUseCase: GetHistoryUseCase
    private val repository = mockk<PlanRadarRepository>(relaxed = true)

    @Before
    fun setUp() {
        getHistoryUseCase = GetHistoryUseCase(repository)
    }

    @Test
    fun `should fetch data successfully`() = runTest {
        //Given
        coEvery { repository.getHistory("city") } returns Result.Success(listOf(fakeData))

        //When
        val result = getHistoryUseCase.invoke("city")

        //Then
        assertEquals(result, Result.Success(listOf(fakeData)))
    }

    @Test
    fun `should handle error case`() = runTest {
        //Given
        coEvery { repository.getHistory("city") } returns Result.Error(Exception("an error occurred."))

        //When
        val result = getHistoryUseCase.invoke("city")

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