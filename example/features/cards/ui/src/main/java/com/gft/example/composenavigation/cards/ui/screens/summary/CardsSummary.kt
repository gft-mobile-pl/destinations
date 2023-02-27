package com.gft.example.composenavigation.cards.ui.screens.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gft.example.composenavigation.cards.data.CardRepositoryMock
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
internal fun CardsSummary(
    modifier: Modifier = Modifier,
    onNavigateToCardDetails: (CardArgument) -> Unit,
    onNavigateToFreezeCard: (CardArgument) -> Unit,
    onNavigateToAccountDetails: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val cardsList = CardRepositoryMock.cardsList.collectAsStateWithLifecycle()

        Text(
            text = "Cards summary",
            style = MaterialTheme.typography.headlineLarge
        )

        cardsList.value.forEach { card ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("Card ${card.id}")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToCardDetails(CardArgument(card.id)) }
                        ) {
                            Text("Details")
                        }
                        if (card.isFrozen) {
                            Button(
                                onClick = {
                                    CardRepositoryMock.unfreezeCard(card.id)
                                }
                            ) {
                                Text("Unfreeze")
                            }
                        } else {
                            Button(
                                onClick = { onNavigateToFreezeCard(CardArgument(card.id)) }
                            ) {
                                Text("Freeze")
                            }
                        }

                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Cross-feature navigation",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "The link below demonstrates how to navigate to some screen of a different feature which " +
                "may not even be our dependency.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { onNavigateToAccountDetails() }
        ) {
            Text("Cross navigate to account details")
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardsSummaryPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardsSummary(
            onNavigateToCardDetails = {},
            onNavigateToFreezeCard = {},
            onNavigateToAccountDetails = {}
        )
    }
}