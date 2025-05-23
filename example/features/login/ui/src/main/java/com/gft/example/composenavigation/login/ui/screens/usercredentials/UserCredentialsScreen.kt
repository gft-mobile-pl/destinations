package com.gft.example.composenavigation.login.ui.screens.usercredentials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun UserCredentialsScreen(
    modifier: Modifier = Modifier,
    onNavigateToOtp: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Login")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = "Password")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { onNavigateToOtp() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Send")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
internal fun UserCredentialsScreenPreview() {
    ComposeMultimoduleNavigationTheme {
        UserCredentialsScreen(onNavigateToOtp = { })
    }
}
