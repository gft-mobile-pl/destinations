package com.gft.example.composenavigation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.destinations.Destination
import com.gft.destinations.navigate
import com.gft.destinations.navigation
import com.gft.destinations.redirect
import com.gft.example.composenavigation.account.ui.navigation.AccountDetailsDestination
import com.gft.example.composenavigation.account.ui.navigation.AccountSummaryDestination
import com.gft.example.composenavigation.account.ui.navigation.accountFeatureSections
import com.gft.example.composenavigation.cards.ui.navigation.NavigateToAccountDetailsRequest
import com.gft.example.composenavigation.cards.ui.navigation.cardFeatureSections
import com.gft.example.composenavigation.login.ui.navigation.LogoutPromptSectionDestination
import com.gft.example.composenavigation.login.ui.navigation.logoutPromptSection

val LoggedInSectionDestination = Destination.withoutArgument()

fun NavGraphBuilder.loggedInSection(
    onNavigateToNextAfterLogout: () -> Unit,
    navController: NavHostController
) {
    val homeScreenDestination = Destination.withoutArgument()

    navigation(
        destination = LoggedInSectionDestination,
        startDestination = homeScreenDestination,
        label = "Logged-in Section"
    ) {
        homeScreenSection(
            sectionDestination = homeScreenDestination,
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
            onNavigateBack = redirect(navController, LogoutPromptSectionDestination),

            // This callback is added just to demonstrate a very rare case of unnamed/context-less navigation.
            // (Generally such navigation should be avoided).
            onNavigationRequest = { request ->
                when (request) {
                    NavigateToAccountDetailsRequest -> navController.navigate(AccountDetailsDestination)
                }
            },

            navController = navController
        )

        accountFeatureSections(navController)

        cardFeatureSections(
            onNavigateToAccountSummary = redirect(navController, AccountSummaryDestination),
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination),
            navController = navController
        )

        logoutPromptSection(
            onNavigateToNextAfterLogout = onNavigateToNextAfterLogout,
            navController = navController
        )
    }
}
