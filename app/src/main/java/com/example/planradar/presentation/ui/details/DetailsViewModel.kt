package com.example.planradar.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.usecase.GetCityWeatherIcon
import com.example.planradar.domain.usecase.GetCityWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val weather: GetCityWeatherUseCase,
    private val icon: GetCityWeatherIcon,
): ViewModel() {

    private val mutableViewState: MutableStateFlow<DetailsState> =
        MutableStateFlow(DetailsState.Loading)
    val assetsViewState: StateFlow<DetailsState> = mutableViewState

    private lateinit var weatherInfo: WeatherResponse

    fun getWeather(city: String, appId: String) {
        viewModelScope.launch {
            when (val result = weather.invoke(city, appId)) {
                is Result.Success -> {
                    weatherInfo = result.data
//                    getWeatherIcon(weatherInfo.weather[0].icon)
                }
                is Result.Error -> {
                    mutableViewState.value = DetailsState.ShowWeatherFailed("Failed loading weather data.")
                }

            }
        }

    }

    private fun getWeatherIcon(iconId: String) {
        viewModelScope.launch {
            when (val result = icon.invoke(iconId)) {
                is Result.Success -> {
                    mutableViewState.value = DetailsState.ShowWeather(weatherInfo)
                }
                is Result.Error -> {}
            }
        }
    }

    sealed class DetailsState {
        object Loading : DetailsState()
        data class ShowWeather(val item: WeatherResponse) : DetailsState()
        data class ShowWeatherFailed(val error: String) : DetailsState()
    }
}