package com.gft.example.composenavigation.ui.screens.welcomescreen

import androidx.lifecycle.ViewModel

class WelcomeScreenViewModel : ViewModel() {
    init {
        println("#Test WelcomeScreenViewModel created!")
    }

    override fun onCleared() {
        println("#Test WelcomeScreenViewModel disposed")
    }
}