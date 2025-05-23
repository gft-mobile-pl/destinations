package com.gft.example.composenavigation.ui.screens.welcomescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun WelcomeScreen(
    onNavigateToNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome to the App!",
                style = MaterialTheme.typography.headlineLarge,
            )
            Button(onClick = {
                onNavigateToNext()
            }) {
                Text("Continue")
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
internal fun WelcomeScreenPreview() {
    ComposeMultimoduleNavigationTheme {
        WelcomeScreen(onNavigateToNext = {})
    }
}
