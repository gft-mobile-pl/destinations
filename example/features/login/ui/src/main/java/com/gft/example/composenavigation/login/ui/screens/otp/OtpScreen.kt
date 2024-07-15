package com.gft.example.composenavigation.login.ui.screens.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    onSuccessfulLogin: () -> Unit,
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
            Text(text = "Otp")
            OutlinedTextField(
                value = "",
                onValueChange = {},
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { onSuccessfulLogin() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Login")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
internal fun OtpScreenPreview() {
    ComposeMultimoduleNavigationTheme {
        OtpScreen(onSuccessfulLogin = { })
    }
}
