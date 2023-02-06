package com.gft.example.composenavigation.cards.ui.screens.details

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun CardDetails(
    modifier: Modifier = Modifier,
    card: CardArgument,
    onNavigateToFreezeCard: (CardArgument) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val cardId = remember { card.cardId }
        Text(
            text = "Card $cardId details",
            style = MaterialTheme.typography.headlineLarge
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Some card description")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onNavigateToFreezeCard(card) }
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
fun CardDetailsPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardDetails(
            card = CardArgument("#1"),
            onNavigateToFreezeCard = {}
        )
    }
}