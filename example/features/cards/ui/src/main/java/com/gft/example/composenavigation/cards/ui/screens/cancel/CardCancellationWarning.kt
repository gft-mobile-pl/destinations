package com.gft.example.composenavigation.cards.ui.screens.cancel

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
import com.gft.example.composenavigation.cards.ui.navigation.CardArgument
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
internal fun CardCancellationWarning(
    card: CardArgument,
    onNavigateToConfirmation: (CardArgument) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Card cancellation",
            style = MaterialTheme.typography.headlineLarge
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Some longer description of the card cancellation process")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onNavigateToConfirmation(card) }
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun CardCancellationWarningPreview() {
    ComposeMultimoduleNavigationTheme() {
        CardCancellationWarning(
            card = CardArgument("#1"),
            onNavigateToConfirmation = {}
        )
    }
}
