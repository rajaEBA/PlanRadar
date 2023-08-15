package com.example.planradar.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.usecase.GetCityWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val weather: GetCityWeatherUseCase,
): ViewModel() {

    private val mutableViewState: MutableStateFlow<DetailsState> =
        MutableStateFlow(DetailsState.Loading)
    val assetsViewState: StateFlow<DetailsState> = mutableViewState

    fun getWeather(city: String, appId: String) {
        viewModelScope.launch {
            when (val result = weather.invoke(city, appId)) {
                is Result.Success -> {
                    mutableViewState.value = DetailsState.ShowWeather(result.data)
                }
                is Result.Error -> {
                    mutableViewState.value = DetailsState.ShowWeatherFailed("Failed loading weather data.")
                }

            }
        }

    }

    sealed class DetailsState {
        object Loading : DetailsState()
        data class ShowWeather(val item: WeatherResponse) : DetailsState()
        data class ShowWeatherFailed(val error: String) : DetailsState()
    }
}