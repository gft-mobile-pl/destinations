package com.gft.example.composenavigation.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.NavHost
import com.gft.destinations.composable
import com.gft.destinations.navigate
import com.gft.example.composenavigation.account.ui.accountsummary.AccountSummary
import com.gft.example.composenavigation.cards.ui.screens.summary.CardsSummary
import com.gft.example.composenavigation.common.theme.ComposeMultimoduleNavigationTheme

private enum class Section(val icon: ImageVector, val label: String, val destination: DestinationWithoutArgument) {
    WIDGETS(Icons.Filled.Home, "Home", Destination.withoutArgument()),
    ACCOUNT(Icons.Filled.Person, "Account", Destination.withoutArgument()),
    CARD(Icons.Filled.Warning, "Card", Destination.withoutArgument())
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToAccountDetails: () -> Unit,
    onNavigateToCardDetails: (String) -> Unit,
    onNavigateToFreezeCard: (String) -> Unit,
    onNavigationRequest: (Any) -> Unit
) {
    val internalNavController: NavHostController = rememberNavController()
    Scaffold(modifier = modifier, bottomBar = {
        NavigationBar(
            modifier = Modifier
                .height(72.dp)
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
        ) {
            val currentBackStackEntry = internalNavController.currentBackStackEntryFlow.collectAsState(internalNavController.currentBackStackEntry)
            Section.values().forEach { section ->
                NavigationBarItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(section.icon, contentDescription = "")
                            Text(text = section.label) // You could use `label` property of NavigationBarItem but the selection marker embraces the icon only (!)
                        }
                    },
                    selected = currentBackStackEntry.value?.destination?.hierarchy?.any { it.id == section.destination.id } == true,
                    onClick = {
                        internalNavController.navigate(section.destination) {
                            popUpTo(internalNavController.graph.findStartDestination().id) {
                                saveState = true // optional (required ONLY IF sections may have inner navigation (better not))
                            }
                            launchSingleTop = true
                            restoreState = true // optional (required ONLY IF sections may have inner navigation (better not))
                        }
                    }
                )
            }
        }
    }) {
        NavHost(navController = internalNavController, startDestination = Section.WIDGETS.destination) {
            composable(Section.WIDGETS.destination) {
                Text("Widgets go here...")
            }
            composable(Section.ACCOUNT.destination) {
                AccountSummary(onNavigateToAccountDetails = onNavigateToAccountDetails)
            }
            composable(Section.CARD.destination) {
                CardsSummary(
                    onNavigateToCardDetails = onNavigateToCardDetails,
                    onNavigateToFreezeCard = onNavigateToFreezeCard
                )
            }
        }
    }
}

@Preview(showSystemUi = false, heightDp = 840)
@Composable
fun HomeScreenPreview() {
    ComposeMultimoduleNavigationTheme() {
        HomeScreen(
            onNavigateToAccountDetails = {},
            onNavigateToCardDetails = {},
            onNavigateToFreezeCard = {},
            onNavigationRequest = {}
        )
    }
}