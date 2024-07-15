package com.gft.example.composenavigation.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.rememberNavController
import com.gft.destinations.Destination
import com.gft.destinations.NavHost
import com.gft.destinations.composable
import com.gft.destinations.navigate
import com.gft.example.composenavigation.account.ui.navigation.accountSummarySection
import com.gft.example.composenavigation.cards.ui.navigation.cardsSummarySection
import com.gft.example.composenavigation.common.ui.navigation.BackPressHandler
import com.gft.example.composenavigation.ui.screens.homescreen.HomeScreen
import com.gft.example.composenavigation.ui.screens.homescreen.HomeScreenSection
import com.gft.example.composenavigation.ui.screens.widgetscreen.WidgetsScreen

fun NavGraphBuilder.homeScreenSection(
    navController: NavController,
    onNavigateToAccountDetails: () -> Unit,
    onNavigationRequest: (Any) -> Unit, // this callback is here to demonstrate a very rare case of unnamed/context-less navigation
    onNavigateBack: () -> Unit,
    sectionDestination: Destination.DestinationWithoutArgument,
) {
    composable(sectionDestination, "Home Screen") {
        val sectionsNavController = rememberNavController()
        val currentBackStackEntry = sectionsNavController.currentBackStackEntryFlow.collectAsState(sectionsNavController.currentBackStackEntry)
        val selectedSection = remember {
            derivedStateOf {
                HomeScreenSection.entries.firstOrNull { section ->
                    currentBackStackEntry.value?.destination?.hierarchy?.any { it.id == section.toDestination().id } == true
                } ?: HomeScreenSection.WIDGETS
            }
        }
        val navigateToSection = { section: HomeScreenSection ->
            sectionsNavController.navigate(section.toDestination()) {
                popUpTo(sectionsNavController.graph.findStartDestination().id) {
                    saveState = true // optional (required ONLY IF sections may have inner navigation (better not))
                }
                launchSingleTop = true
                restoreState = true // optional (required ONLY IF sections may have inner navigation (better not))
            }
        }

        BackPressHandler(onBackPressed = onNavigateBack)

        HomeScreen(
            selectedSection = selectedSection,
            onSectionSelected = navigateToSection,
            sectionsNavHostBuilder = { modifier ->
                NavHost(
                    navController = sectionsNavController,
                    startDestination = HomeScreenSection.WIDGETS.toDestination(),
                    modifier = modifier,
                ) {
                    HomeScreenSection.entries.forEach { section ->
                        when (section) {
                            HomeScreenSection.WIDGETS -> composable(section.toDestination(), "Home") {
                                WidgetsScreen(
                                    onNavigationRequest = onNavigationRequest,
                                    navController = navController
                                )
                            }

                            HomeScreenSection.ACCOUNT -> accountSummarySection(
                                navController = navController,
                                sectionDestination = section.toDestination()
                            )

                            HomeScreenSection.CARD -> cardsSummarySection(
                                navController = navController,
                                sectionDestination = section.toDestination(),
                                onNavigateToAccountSummary = { navigateToSection(HomeScreenSection.ACCOUNT) },
                                onNavigateToAccountDetails = onNavigateToAccountDetails
                            )
                        }
                    }
                }
            }
        )

    }
}

private val WidgetsDestination = Destination.withoutArgument()
private val AccountDestination = Destination.withoutArgument()
private val CardDestination = Destination.withoutArgument()
private fun HomeScreenSection.toDestination() = when (this) {
    HomeScreenSection.WIDGETS -> WidgetsDestination
    HomeScreenSection.ACCOUNT -> AccountDestination
    HomeScreenSection.CARD -> CardDestination
}
