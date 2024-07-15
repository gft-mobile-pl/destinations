package com.gft.example.composenavigation.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

internal enum class HomeScreenSection(val icon: ImageVector, val label: String) {
    WIDGETS(Icons.Filled.Home, "Home"),
    ACCOUNT(Icons.Filled.Person, "Account"),
    CARD(Icons.Filled.Warning, "Card")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedSection: State<HomeScreenSection>,
    onSectionSelected: (HomeScreenSection) -> Unit,
    sectionsNavHostBuilder: @Composable (modifier: Modifier) -> Unit,
) {
    Scaffold(modifier = modifier, bottomBar = {
        NavigationBar(
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
        ) {
            HomeScreenSection.entries.forEach { section ->
                NavigationBarItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(section.icon, contentDescription = "")
                            Text(text = section.label) // You could use `label` property of NavigationBarItem but the selection marker embraces the icon only (!)
                        }
                    },
                    selected = selectedSection.value == section,
                    onClick = { onSectionSelected(section) }
                )
            }
        }
    }) { innerPadding ->
        sectionsNavHostBuilder(Modifier.padding(innerPadding))
    }
}
