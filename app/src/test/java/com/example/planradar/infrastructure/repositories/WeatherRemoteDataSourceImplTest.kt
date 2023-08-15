package com.example.planradar.infrastructure.repositories

import com.example.planradar.domain.common.Result
import com.example.planradar.infrastructure.api.PlanRadarApi
import com.example.planradar.infrastructure.api.WeatherResponseEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRemoteDataSourceImplTest {

    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private val service = mockk<PlanRadarApi>(relaxed = true)
    private val weatherResponse = mockk<WeatherResponseEntity>()

    @Before
    fun setUp() {
        remoteDataSource = WeatherRemoteDataSourceImpl(
            service = service,
        )
    }

    @Test
    fun `should return weather successfully`() = runTest {
        // Given
       // val responseList = listOf(fakeArticleInfo)
        val response = Response.success(weatherResponse)
        coEvery { service.getWeather(any()) } returns response

        // When
        val result = remoteDataSource.getWeatherOfCity("city")

        // Then
        assertEquals(response.body(), result)
    }

    @Test(expected = IOException::class)
    fun `should failed return weather `() = runTest {
        // Given
        val errorMessage = "Error message"
        coEvery { service.getWeather(any()) } throws IOException(errorMessage)

        // When
        val result = remoteDataSource.getWeatherOfCity("city")

        // Then
        assertEquals(Result.Error(Exception(errorMessage)), result)
    }

}