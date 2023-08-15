package com.example.planradar.presentation.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planradar.domain.model.City
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CityViewModel: ViewModel() {

    private val mutableViewState = MutableSharedFlow<CityState>(replay = 0)
    val cityViewState: SharedFlow<CityState> = mutableViewState

    fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.AddCity -> {
                    mutableViewState.emit(CityState.CityAdded(event.item))
                }
                is Event.ShowDetails -> {
                    mutableViewState.emit(CityState.DetailsLoaded(event.item))
                }
            }
        }
    }

    sealed class Event {
        data class ShowDetails(val item: City) : Event()
        data class AddCity(val item: String) : Event()
    }

    sealed class CityState {
        data class DetailsLoaded(val item: City) : CityState()
        data class CityAdded(val city: String) : CityState()
    }
}