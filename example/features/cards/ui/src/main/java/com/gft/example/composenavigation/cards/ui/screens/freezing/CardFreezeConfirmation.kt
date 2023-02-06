package com.gft.example.composenavigation.cards.ui.screens.freezing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun CardFreezeConfirmation(
    modifier: Modifier = Modifier,
    card: CardArgument,
    onNavigateToNextAfterCardFrozen: () -> Unit,
    onNavigateToNextAfterCardFreezeAborted: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Do you really want to freeze your ${card.cardId} card?",
            style = MaterialTheme.typography.headlineLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onNavigateToNextAfterCardFrozen() }
            ) {
                Text("Freeze")
            }
            Button(
                onClick = { onNavigateToNextAfterCardFreezeAborted() }
            ) {
                Text("Abort")
            }
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardFreezeConfirmationPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardFreezeConfirmation(
            card = CardArgument("#1"),
            onNavigateToNextAfterCardFrozen = {},
            onNavigateToNextAfterCardFreezeAborted = {}
        )
    }
}