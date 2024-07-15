package com.gft.example.composenavigation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gft.example.composenavigation.ui.navigation.ApplicationNavHost

@Composable
fun MainView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ApplicationNavHost()
    }
}
