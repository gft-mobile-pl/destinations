package com.gft.example.composenavigation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.destinations.Destination
import com.gft.destinations.composable
import com.gft.destinations.navigate
import com.gft.destinations.navigation
import com.gft.destinations.popBackStack
import com.gft.destinations.redirect
import com.gft.example.composenavigation.account.ui.navigation.AccountDetailsDestination
import com.gft.example.composenavigation.account.ui.navigation.accountFeatureSections
import com.gft.example.composenavigation.cards.ui.navigation.NavigateToAccountDetailsRequest
import com.gft.example.composenavigation.cards.ui.navigation.cardFeatureSections
import com.gft.example.composenavigation.common.ui.navigation.BackPressHandler
import com.gft.example.composenavigation.login.ui.navigation.LogoutPromptSectionDestination
import com.gft.example.composenavigation.login.ui.navigation.logoutPromptSection
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
            BackPressHandler(
                onBackPressed = redirect(navController, LogoutPromptSectionDestination)
            )

            HomeScreen(
                onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
                navController = navController,

                /* This callback is here just to demonstrate a very rare case of unnamed/context-less navigation
                *  which generally should be avoided. */
                onNavigationRequest = { request ->
                    when (request) {
                        NavigateToAccountDetailsRequest -> navController.navigate(AccountDetailsDestination)
                    }
                },
            )
        }

        accountFeatureSections(navController)

        cardFeatureSections(
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
            navController = navController
        )

        logoutPromptSection(
            onNavigateToNextAfterLogout = onNavigateToNextAfterLogout,
            navController = navController
        )
    }
}