package com.gft.example.composenavigation.account.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.composable
import com.gft.destinations.redirect
import com.gft.example.composenavigation.account.ui.screens.accountdetails.AccountDetails
import com.gft.example.composenavigation.account.ui.screens.accountsummary.AccountSummary
import com.gft.example.composenavigation.account.ui.screens.widget.AccountFeatureWidget

/**
 * This file could be easily split into 4 files if the graphs gets bigger:
 * - AccountFeatureSections
 * - AccountSummarySection
 * - AccountDetailsSection
 * - AccountFeatureWidget.
 */

val AccountSummaryDestination = Destination.withoutArgument()
val AccountDetailsDestination = Destination.withoutArgument()
fun NavGraphBuilder.accountFeatureSections(navController: NavController) {
    accountSummarySection(navController, AccountSummaryDestination)
    accountDetailsSection(navController, AccountDetailsDestination)
}

/**
 * Account summary section.
 */
fun NavGraphBuilder.accountSummarySection(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument,
) {
    composable(sectionDestination, "Account Summary") {
        AccountSummary(
            onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination)
        )
    }
}

/**
 * Account details section.
 */
internal fun NavGraphBuilder.accountDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument,
) {
    composable(sectionDestination, "Account Details") {
        AccountDetails()
    }
}

/**
 * Cards widget.
 */
@Composable
fun AccountFeatureWidget(navController: NavController) {
    AccountFeatureWidget(
        onNavigateToAccountDetails = redirect(navController, AccountDetailsDestination)
    )
}
