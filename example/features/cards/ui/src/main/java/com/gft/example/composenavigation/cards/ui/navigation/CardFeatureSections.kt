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
 * This file could be easily split into 6 files if the graphs gets bigger:
 * - CardFeatureSections
 * - CardsSummarySection
 * - CardDetailsSection
 * - FreezeCardSection
 * - CancelCardSection
 * - Cards widget.
 */

/**
 * Common Card argument used by many sections.
 */
@Parcelize
data class CardArgument(val cardId: String) : Parcelable

fun NavGraphBuilder.cardFeatureSections(
    navController: NavController,
    onNavigateToAccountSummary: () -> Unit, // example of cross-feature navigation
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    cardsSummarySection(navController, onNavigateToAccountSummary, onNavigateToAccountDetails)
    cardDetailsSection(navController, onNavigateToAccountDetails)
    freezeCardSection(navController)
}

/**
 * Cards summary section (aka cards list)
 */
val CardsSummarySectionDestination = Destination.withoutArgument()
internal fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    onNavigateToAccountSummary: () -> Unit,
    onNavigateToAccountDetails: () -> Unit
) = cardsSummarySection(navController, CardsSummarySectionDestination, onNavigateToAccountSummary, onNavigateToAccountDetails)

fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument,
    onNavigateToAccountSummary: () -> Unit, // example of cross-feature navigation
    onNavigateToAccountDetails: () -> Unit // example of cross-feature navigation
) {
    composable(sectionDestination) {
        CardsSummary(
            onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination),
            onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
            onNavigateToAccountSummary = onNavigateToAccountSummary,
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
    val cardDetailsDestination = Destination.withArgument<CardArgument>()
    val cancelCardDestination = Destination.withArgument<CardArgument>()

    navigation(
        destination = sectionDestination,
        startDestination = cardDetailsDestination
    ) {
        composable(cardDetailsDestination) { card ->
            CardDetails(
                card = card,
                onNavigateToAccountDetails = onNavigateToAccountDetails,
                onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
                onNavigateToCancelCard = redirect(navController, cancelCardDestination)
            )
        }

        cancelCardSection(
            navController = navController,
            onNavigateToNextAfterCardCancelled = {
                navController.popBackStack(destination = sectionDestination, inclusive = true)
            },
            sectionDestination = cancelCardDestination
        )
    }
}

/**
 * Freeze card section
 */
val FreezeCardSectionDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.freezeCardSection(navController: NavController) = freezeCardSection(navController, FreezeCardSectionDestination)

fun NavGraphBuilder.freezeCardSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
    val freezeCardWarningDestination = Destination.withArgument<CardArgument>()
    val freezeCardConfirmationDestination = Destination.withArgument<CardArgument>()

    navigation(
        destination = sectionDestination,
        startDestination = freezeCardWarningDestination
    ) {
        composable(freezeCardWarningDestination) { arg ->
            CardFreezeWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    freezeCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(freezeCardConfirmationDestination) { arg ->
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
fun NavGraphBuilder.cancelCardSection(
    navController: NavController,
    onNavigateToNextAfterCardCancelled: () -> Unit,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>
) {
    val cancelCardWarningDestination = Destination.withArgument<CardArgument>()
    val cancelCardConfirmationDestination = Destination.withArgument<CardArgument>()

    navigation(
        destination = sectionDestination,
        startDestination = cancelCardWarningDestination
    ) {
        composable(cancelCardWarningDestination) { arg ->
            CardCancellationWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    cancelCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(cancelCardConfirmationDestination) { arg ->
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