package com.gft.example.composenavigation.ui.screens.homescreen

sealed interface HomeScreenViewEvent {
    object OnBackClicked : HomeScreenViewEvent
}

