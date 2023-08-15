# My Android App

This is a Android app that demonstrates how to display a list of cities, details of weather of city, also, show history

## Features

- Displays a list of items using RecyclerView in CityFragment(add and remove[swipe to right])
- Display city's weather
- History for a city

## ViewModel

- CityViewModel
  A ViewModel class responsible for providing city data to the UI.
  It uses a shared flow called cityViewState to communicate changes in the UI state, which observers can subscribe to.
  The class responds to different events, such as adding a city or showing city details/history, by emitting corresponding CityState instances using mutableViewState.
  These states represent different UI conditions, such as details loaded, history loaded, or city added.

- DetailViewModel
  A ViewModel class responsible for handling weather of city related actions and state.
  It utilizes the weather use case, allowing it to fetch weather information for the given city.

- HistoryViewModel
  A ViewModel class responsible for handling history-related actions and state.
  The class communicates UI state changes via a historyViewState StateFlow.
  It provides functions to fetch historical data (getAllHistory) and respond to events like showing detailed weather (onEvent).

## how

I used Clean Architecture with three distinct layers - infrastructure, domain, and presentation - to separate logic from the view.
Koin was employed for dependency injection, ensuring modularity, testability, and scalability of the application.

