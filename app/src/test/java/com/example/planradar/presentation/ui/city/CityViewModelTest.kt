package com.example.planradar.presentation.ui.city

import app.cash.turbine.test
import com.example.planradar.domain.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityViewModelTest {

    private lateinit var viewModel: CityViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = CityViewModel()
    }

    @Test
    fun `should add a city`() = runTest {

        viewModel.cityViewState.test {
            viewModel.onEvent(CityViewModel.Event.AddCity("city"))
            assertThat(awaitItem()).isEqualTo(CityViewModel.CityState.CityAdded("city"))
        }
    }

    @Test
    fun `should show details`() = runTest {

        viewModel.cityViewState.test {
            viewModel.onEvent(CityViewModel.Event.ShowDetails(City("city")))
            assertThat(awaitItem()).isEqualTo(CityViewModel.CityState.DetailsLoaded(City("city")))
        }
    }

    @Test
    fun `should show history`() = runTest {

        viewModel.cityViewState.test {
            viewModel.onEvent(CityViewModel.Event.ShowHistory(City("city")))
            assertThat(awaitItem()).isEqualTo(CityViewModel.CityState.HistoryLoaded(City("city")))
        }
    }
}