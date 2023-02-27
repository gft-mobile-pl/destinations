package com.gft.example.composenavigation.cards.ui.screens.freezing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun CardFreezeWarning(
    modifier: Modifier = Modifier,
    onNavigateToConfirmation: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Card freezing instructions",
            style = MaterialTheme.typography.headlineLarge
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Some longer description of the card freezing")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onNavigateToConfirmation() }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardFreezeWarningPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardFreezeWarning(
            onNavigateToConfirmation = {}
        )
    }
}
