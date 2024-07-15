package com.gft.example.composenavigation.cards.ui.screens.freezing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.cards.data.CardRepositoryMock
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
internal fun CardFreezeConfirmation(
    card: CardArgument,
    onNavigateToNextAfterCardFrozen: () -> Unit,
    onNavigateToNextAfterCardFreezeAborted: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Do you really want to freeze your ${card.id} card?",
            style = MaterialTheme.typography.headlineLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    CardRepositoryMock.freezeCard(card.id)
                    onNavigateToNextAfterCardFrozen()
                }
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
    ComposeMultimoduleNavigationTheme {
        CardFreezeConfirmation(
            card = CardArgument("#1"),
            onNavigateToNextAfterCardFrozen = {},
            onNavigateToNextAfterCardFreezeAborted = {}
        )
    }
}
