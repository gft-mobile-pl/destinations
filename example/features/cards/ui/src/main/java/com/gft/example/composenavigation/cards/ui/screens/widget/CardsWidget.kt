package com.gft.example.composenavigation.cards.ui.screens.widget

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gft.example.composenavigation.cards.data.CardRepositoryMock
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
internal fun CardsFeatureWidget(
    modifier: Modifier = Modifier,
    onNavigateToCardDetails: (CardArgument) -> Unit,
    onNavigateToAccountDetails: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        val cardsList = CardRepositoryMock.cardsList.collectAsStateWithLifecycle()

        Card {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Text(
                    text = "Cards widget",
                    style = MaterialTheme.typography.headlineLarge
                )
                cardsList.value.forEach { card ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Card ${card.id}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = { onNavigateToCardDetails(CardArgument(card.id)) }
                        ) {
                            Text("Details")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "Cross-feature navigation",
                    style = MaterialTheme.typography.headlineMedium
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
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardsWidgetPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardsFeatureWidget(
            onNavigateToCardDetails = {},
            onNavigateToAccountDetails = {}
        )
    }
}