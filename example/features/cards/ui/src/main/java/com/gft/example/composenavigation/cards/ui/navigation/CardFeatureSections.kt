package com.gft.example.composenavigation.cards.ui.navigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import com.gft.destinations.composable
import com.gft.example.composenavigation.cards.ui.screens.details.CardDetails
import kotlinx.parcelize.Parcelize

/**
 * This file could be easily splited into 4 files if the graphs gets bigger:
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

fun cardFeatureSections(navController: NavController) {

}

/**
 * Card details section.
 */
val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()
fun NavGraphBuilder.cardDetailsSection(
    navController: NavController
) {
    composable(CardDetailsSectionDestination) { card ->
        CardDetails(card = card, onNavigateToFreezeCard = {})
    }
}

/**
 * Freeze card section
 */
val FreezeCardSectionDestination = Destination.withArgument<CardArgument>()

fun NavGraphBuilder.freezeCardSection(
    onNavigateToNextAfterCardFrozen: () -> Unit
) = freezeCardGraph(onNavigateToNextAfterCardFrozen)

internal fun freezeCardGraph(
    onNavigateToNextAfterCardFrozen: () -> Unit
) {

}