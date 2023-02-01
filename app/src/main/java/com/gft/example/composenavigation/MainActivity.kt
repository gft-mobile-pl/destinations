package com.gft.example.composenavigation

import android.app.Fragment
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme
import com.gft.example.composenavigation.ui.navigation.ApplicationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMultimoduleNavigationTheme() {
                MainScreen()
            }
        }


    }


}

@Composable
fun MainScreen() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ApplicationGraph()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreview() {
    ComposeMultimoduleNavigationTheme {
        MainScreen()
    }
}