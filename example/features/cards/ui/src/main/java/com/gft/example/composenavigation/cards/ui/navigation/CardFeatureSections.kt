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

fun NavGraphBuilder.cardFeatureSections(navController: NavController) {
    cardsSummarySection(navController, CardsSummarySectionDestination)
    cardDetailsSection(navController, CardDetailsSectionDestination)
    freezeCardSection(navController, FreezeCardSectionDestination)
}

/**
 * Cards summary section (aka cards list)
 */
val CardsSummarySectionDestination = Destination.withoutArgument()
fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    starDestination: DestinationWithoutArgument
) {
    composable(starDestination) {
        CardsSummary(
            onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination),
            onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination)
        )
    }
}

/**
 * Card details section.
 */
val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()
fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
    composable(sectionDestination) { card ->
        CardDetails(
            card = card,
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