package com.gft.example.composenavigation.cards.ui.navigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithRequiredArgument
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.composable
import com.gft.destinations.navigation
import com.gft.destinations.popBackStack
import com.gft.destinations.redirect
import com.gft.example.composenavigation.cards.ui.screens.details.CardDetails
import com.gft.example.composenavigation.cards.ui.screens.freezing.CardFreezeConfirmation
import com.gft.example.composenavigation.cards.ui.screens.freezing.CardFreezeWarning
import com.gft.example.composenavigation.cards.ui.screens.summary.CardsSummary
import kotlinx.parcelize.Parcelize

/**
 * This file could be easily split into 4 files if the graphs gets bigger:
 * - CardFeatureSections
 * - CardsSummarySection
 * - CardDetailsSection
 * - FreezeCardSection.
 */

/**
 * Common Card argument used by many sections.
 */
@Parcelize
data class CardArgument(val cardId: String) : Parcelable

fun NavGraphBuilder.cardFeatureSections(
    navController: NavController,
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    cardsSummarySection(navController, onNavigateToAccountDetails)
    cardDetailsSection(navController, onNavigateToAccountDetails)
    freezeCardSection(navController)
}

/**
 * Cards summary section (aka cards list)
 */
val CardsSummarySectionDestination = Destination.withoutArgument()
internal fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    onNavigateToAccountDetails: () -> Unit
) = cardsSummarySection(navController, CardsSummarySectionDestination, onNavigateToAccountDetails)

fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    starDestination: DestinationWithoutArgument,
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    composable(starDestination) {
        CardsSummary(
            onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination),
            onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
            onNavigateToAccountDetails = onNavigateToAccountDetails
        )
    }
}

/**
 * Card details section.
 */
val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    onNavigateToAccountDetails: () -> Unit
) = cardDetailsSection(navController, CardDetailsSectionDestination, onNavigateToAccountDetails)

fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>,
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    composable(sectionDestination) { card ->
        CardDetails(
            card = card,
            onNavigateToAccountDetails = onNavigateToAccountDetails,
            onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination)
        )
    }
}

/**
 * Freeze card section
 */
val FreezeCardSectionDestination = Destination.withArgument<CardArgument>()
private val FreezeCardWarningDestination = Destination.withArgument<CardArgument>()
private val FreezeCardConfirmationDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.freezeCardSection(navController: NavController) = freezeCardSection(navController, FreezeCardSectionDestination)
fun NavGraphBuilder.freezeCardSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
    navigation(
        destination = sectionDestination,
        startDestination = FreezeCardWarningDestination
    ) {
        composable(FreezeCardWarningDestination) { arg ->
            CardFreezeWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    FreezeCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(FreezeCardConfirmationDestination) { arg ->
            CardFreezeConfirmation(
                card = arg,
                onNavigateToNextAfterCardFrozen = { navController.popBackStack(sectionDestination, true) },
                onNavigateToNextAfterCardFreezeAborted = { navController.popBackStack(sectionDestination, true) },
            )
        }
    }
}