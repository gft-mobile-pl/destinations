package com.gft.example.composenavigation.cards.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithRequiredArgument
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.composable
import com.gft.destinations.navigation
import com.gft.destinations.popBackStack
import com.gft.destinations.redirect
import com.gft.example.composenavigation.cards.ui.screens.cancel.CardCancellationConfirmation
import com.gft.example.composenavigation.cards.ui.screens.cancel.CardCancellationWarning
import com.gft.example.composenavigation.cards.ui.screens.details.CardDetails
import com.gft.example.composenavigation.cards.ui.screens.freezing.CardFreezeConfirmation
import com.gft.example.composenavigation.cards.ui.screens.freezing.CardFreezeWarning
import com.gft.example.composenavigation.cards.ui.screens.summary.CardsSummary
import com.gft.example.composenavigation.cards.ui.screens.widget.CardsFeatureWidget
import kotlinx.parcelize.Parcelize

/**
 * This file could be easily split into 4 files if the graphs gets bigger:
 * - CardFeatureSections
 * - CardsSummarySection
 * - CardDetailsSection
 * - FreezeCardSection
 * - Cards widget.
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
    sectionDestination: DestinationWithoutArgument,
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    composable(sectionDestination) {
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
private val CardDetailsDestination = Destination.withArgument<CardArgument>()
private val CancelCardSectionDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    onNavigateToAccountDetails: () -> Unit
) = cardDetailsSection(navController, CardDetailsSectionDestination, onNavigateToAccountDetails)

fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>,
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    navigation(
        destination = sectionDestination,
        startDestination = CardDetailsDestination
    ) {
        composable(CardDetailsDestination) { card ->
            CardDetails(
                card = card,
                onNavigateToAccountDetails = onNavigateToAccountDetails,
                onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
                onNavigateToCancelCard = redirect(navController, CancelCardSectionDestination)
            )
        }

        cancelCardSection(
            navController = navController,
            onNavigateToNextAfterCardCancelled = {
                navController.popBackStack(destination = sectionDestination, inclusive = true)
            },
            sectionDestination = CancelCardSectionDestination
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

/**
 * Cancel card section
 */
private val CancelCardWarningDestination = Destination.withArgument<CardArgument>()
private val CancelCardConfirmationDestination = Destination.withArgument<CardArgument>()
fun NavGraphBuilder.cancelCardSection(
    navController: NavController,
    onNavigateToNextAfterCardCancelled: () -> Unit,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
    navigation(
        destination = sectionDestination,
        startDestination = CancelCardWarningDestination
    ) {
        composable(CancelCardWarningDestination) { arg ->
            CardCancellationWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    CancelCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(CancelCardConfirmationDestination) { arg ->
            CardCancellationConfirmation(
                card = arg,
                onNavigateToNextAfterCardCancelled = onNavigateToNextAfterCardCancelled,
                onNavigateToNextAfterCardCancellationAborted = { navController.popBackStack(sectionDestination, true) },
            )
        }
    }
}

/**
 * Cards widget.
 */
object NavigateToAccountDetailsRequest

@Composable
fun CardsFeatureWidget(
    navController: NavController,
    onNavigateToAccountDetails: (NavigateToAccountDetailsRequest) -> Unit
) {
    CardsFeatureWidget(
        onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination),
        onNavigateToAccountDetails = { onNavigateToAccountDetails(NavigateToAccountDetailsRequest) }
    )
}