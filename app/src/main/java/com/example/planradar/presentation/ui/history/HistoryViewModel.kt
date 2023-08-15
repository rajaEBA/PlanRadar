package com.example.planradar.presentation.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planradar.domain.common.Result
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.domain.usecase.GetHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val history: GetHistoryUseCase
) : ViewModel() {

    private val mutableViewState: MutableStateFlow<HistoryState> =
        MutableStateFlow(HistoryState.Loading)
    val historyViewState: StateFlow<HistoryState> = mutableViewState

    fun getAllHistory(city: String) {
        viewModelScope.launch {
            when (val result = history.invoke(city)) {
                is Result.Success -> {
                    mutableViewState.value = HistoryState.ShowHistory(result.data)
                }
                is Result.Error -> {
                    mutableViewState.value = HistoryState.ShowHistoryFailed("Loading failed.")
                }
            }
        }

    }

    fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.ShowDetails -> mutableViewState.value =
                    HistoryState.ShowDetails(event.item)
            }
        }
    }

    sealed class Event {
        data class ShowDetails(val item: WeatherResponse) : Event()
    }

    sealed class HistoryState {
        object Loading : HistoryState()
        data class ShowHistory(val item: List<WeatherResponse>) : HistoryState()
        data class ShowDetails(val item: WeatherResponse) : HistoryState()
        data class ShowHistoryFailed(val error: String) : HistoryState()
    }
}