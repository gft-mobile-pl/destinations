package com.gft.example.composenavigation.account.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.composable
import com.gft.destinations.redirect
import com.gft.example.composenavigation.account.ui.screens.accountdetails.AccountDetails
import com.gft.example.composenavigation.account.ui.screens.accountsummary.AccountSummary

fun NavGraphBuilder.accountFeatureSections(navController: NavController) {
    accountSummary(navController)
    accountDetails(navController)
}

/**
 * Account summary section.
 */
val AccountSummaryDestination = Destination.withoutArgument()
internal fun NavGraphBuilder.accountSummary(navController: NavController) = accountSummary(navController, AccountSummaryDestination)
fun NavGraphBuilder.accountSummary(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument
) {
    composable(sectionDestination) {
        AccountSummary(
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination)
        )
    }
}

/**
 * Account details section.
 */
val AccountDetailsDestination = Destination.withoutArgument()
internal fun NavGraphBuilder.accountDetails(navController: NavController) = accountDetails(navController, AccountDetailsDestination)
fun NavGraphBuilder.accountDetails(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument
) {
    composable(sectionDestination) {
        AccountDetails()
    }
}

