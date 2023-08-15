package com.example.planradar.presentation.ui.history

import app.cash.turbine.test
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.usecase.GetHistoryUseCase
import com.example.planradar.presentation.ui.history.HistoryViewModel.HistoryState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    private lateinit var viewModel: HistoryViewModel

    private val history = mockk<GetHistoryUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `should show history`() = runTest {
        // Given
        coEvery { history.invoke(any()) } coAnswers { Result.Success(listOf(fakeData)) }

        viewModel = HistoryViewModel(history)
        viewModel.getAllHistory("city")

        viewModel.historyViewState.test {
            assertThat(awaitItem()).isEqualTo(HistoryState.Loading)
            assertThat(awaitItem()).isEqualTo(HistoryState.ShowHistory(listOf(fakeData)))
        }
    }

    @Test
    fun `should show details`() = runTest {
        // Given
        coEvery { history.invoke(any()) } coAnswers { Result.Success(listOf(fakeData)) }

        viewModel = HistoryViewModel(history)
        viewModel.getAllHistory("city")

        viewModel.historyViewState.test {
            assertThat(awaitItem()).isEqualTo(HistoryState.Loading)
            assertThat(awaitItem()).isEqualTo(HistoryState.ShowHistory(listOf(fakeData)))
            viewModel.onEvent(HistoryViewModel.Event.ShowDetails(fakeData))
            assertThat(awaitItem()).isEqualTo(HistoryState.ShowDetails(fakeData))
        }
    }

    @Test
    fun `should failed to show details`() = runTest {
        // Given
        coEvery { history.invoke("city") } coAnswers { Result.Error(Exception("Loading failed.")) }

        viewModel = HistoryViewModel(history)
        viewModel.getAllHistory("city")

        viewModel.historyViewState.test {
            assertThat(awaitItem()).isEqualTo(HistoryState.Loading)
            assertThat(awaitItem()).isEqualTo(HistoryState.ShowHistoryFailed("Loading failed."))
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