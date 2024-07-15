package com.gft.example.composenavigation.cards.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
internal fun CardDetails(
    modifier: Modifier = Modifier,
    card: CardArgument,
    onNavigateToAccountDetails: () -> Unit,
    onNavigateToFreezeCard: (CardArgument) -> Unit,
    onNavigateToCancelCard: (CardArgument) -> Unit,
) {
    val cardDetails = CardRepositoryMock.streamCardDetails(card.id)
        .collectAsStateWithLifecycle(initialValue = CardRepositoryMock.getCardDetails(card.id))

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Card ${cardDetails.value.id} details",
            style = MaterialTheme.typography.headlineLarge
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Some card description")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (cardDetails.value.isFrozen) {
                        Button(
                            onClick = { CardRepositoryMock.unfreezeCard(cardDetails.value.id) }
                        ) {
                            Text("Unfreeze")
                        }
                    } else {
                        Button(
                            onClick = { onNavigateToFreezeCard(CardArgument(cardDetails.value.id)) }
                        ) {
                            Text("Freeze")
                        }
                    }
                    Button(
                        onClick = { onNavigateToCancelCard(CardArgument(cardDetails.value.id)) }
                    ) {
                        Text("Cancel card")
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
fun CardDetailsPreview() {
    ComposeMultimoduleNavigationTheme {
        CardDetails(
            card = CardArgument("#1"),
            onNavigateToAccountDetails = {},
            onNavigateToFreezeCard = {},
            onNavigateToCancelCard = {}
        )
    }
}
