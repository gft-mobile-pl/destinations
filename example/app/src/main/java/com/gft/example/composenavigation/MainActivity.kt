package com.gft.example.composenavigation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme
import com.gft.example.composenavigation.ui.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMultimoduleNavigationTheme {
                MainView()
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun DefaultPreview() {
    ComposeMultimoduleNavigationTheme {
        MainView()
    }
}
