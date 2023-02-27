package com.gft.example.composenavigation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.destinations.Destination
import com.gft.destinations.composable
import com.gft.destinations.navigation
import com.gft.example.composenavigation.cards.ui.navigation.cardFeatureSections
import com.gft.example.composenavigation.ui.screens.homescreen.HomeScreen

val LoggedInSectionDestination = Destination.withoutArgument()
private val HomeScreenDestination = Destination.withoutArgument()

fun NavGraphBuilder.loggedInSection(
    onNavigateToNextAfterLogout: () -> Unit,
    navController: NavHostController
) {
    navigation(
        destination = LoggedInSectionDestination,
        startDestination = HomeScreenDestination
    ) {
        composable(HomeScreenDestination) {
            HomeScreen(
                onNavigateToAccountDetails = {},
                onNavigateToCardDetails = {},
                onNavigateToFreezeCard = {},
                onNavigationRequest = {}
            )
        }

        cardFeatureSections(navController)
    }
}