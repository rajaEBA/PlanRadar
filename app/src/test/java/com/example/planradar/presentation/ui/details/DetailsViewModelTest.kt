package com.example.planradar.presentation.ui.details

import app.cash.turbine.test
import com.example.planradar.domain.usecase.GetCityWeatherUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private lateinit var viewModel: DetailsViewModel

    private val weather = mockk<GetCityWeatherUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `should show details`() = runTest {
        // Given
        coEvery { weather.invoke(any()) } coAnswers { Result.Success(fakeData) }

        viewModel = DetailsViewModel(weather)
        viewModel.getWeather("city")

        viewModel.detailsViewState.test {
            assertThat(awaitItem()).isEqualTo(DetailsViewModel.DetailsState.Loading)
            assertThat(awaitItem()).isEqualTo(DetailsViewModel.DetailsState.ShowWeather(fakeData))
        }
    }

    @Test
    fun `should failed to show details`() = runTest {
        // Given
        coEvery { weather.invoke("city") } coAnswers { Result.Error(Exception("Failed loading weather data.")) }

        viewModel = DetailsViewModel(weather)
        viewModel.getWeather("city")

        viewModel.detailsViewState.test {
            assertThat(awaitItem()).isEqualTo(DetailsViewModel.DetailsState.Loading)
            assertThat(awaitItem()).isEqualTo(DetailsViewModel.DetailsState.ShowWeatherFailed("Failed loading weather data."))
        }
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