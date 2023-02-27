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

fun NavGraphBuilder.accountFeatureSections(navController: NavController) {
    accountSummarySection(navController)
    accountDetailsSection(navController)
}

/**
 * Account summary section.
 */
val AccountSummaryDestination = Destination.withoutArgument()
internal fun NavGraphBuilder.accountSummarySection(navController: NavController) = accountSummarySection(navController, AccountSummaryDestination)
fun NavGraphBuilder.accountSummarySection(
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
internal fun NavGraphBuilder.accountDetailsSection(navController: NavController) = accountDetailsSection(navController, AccountDetailsDestination)
fun NavGraphBuilder.accountDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument
) {
    composable(sectionDestination) {
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

