package com.gft.example.composenavigation.account.ui.accountsummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun AccountSummary(
    modifier: Modifier = Modifier,
    onNavigateToAccountDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Account summary",
            style = MaterialTheme.typography.headlineLarge
        )
        Button(
            onClick = { onNavigateToAccountDetails() }
        ) {
            Text("Show account details")
        }
    }
}

@Preview(showSystemUi = false, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    ComposeMultimoduleNavigationTheme() {
        AccountSummary(onNavigateToAccountDetails = { })
    }
}