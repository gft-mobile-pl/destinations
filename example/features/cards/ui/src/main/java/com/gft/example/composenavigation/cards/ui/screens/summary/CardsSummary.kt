package com.gft.example.composenavigation.cards.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun CardsSummary(
    modifier: Modifier = Modifier,
    onNavigateToCardDetails: (String) -> Unit,
    onNavigateToFreezeCard: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Cards summary",
            style = MaterialTheme.typography.headlineLarge
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Card #1")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onNavigateToCardDetails("#1") }
                    ) {
                        Text("Details")
                    }
                    Button(
                        onClick = { onNavigateToFreezeCard("#1") }
                    ) {
                        Text("Freeze")
                    }
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Card #2")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onNavigateToCardDetails("#2") }
                    ) {
                        Text("Details")
                    }
                    Button(
                        onClick = { onNavigateToFreezeCard("#2") }
                    ) {
                        Text("Freeze")
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardsSummaryPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardsSummary(
            onNavigateToCardDetails = {},
            onNavigateToFreezeCard = {}
        )
    }
}