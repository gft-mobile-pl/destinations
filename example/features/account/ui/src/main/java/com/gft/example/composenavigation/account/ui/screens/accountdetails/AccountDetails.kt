package com.gft.example.composenavigation.account.ui.screens.accountdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
internal fun AccountDetails(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Account details",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Some details of the user's account.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
private fun AccountDetailsPreview() {
    ComposeMultimoduleNavigationTheme() {
        AccountDetails()
    }
}