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
data class CardArgument(val id: String) : Parcelable

/**
 * All module's sections
 */
internal val CardsSummarySectionDestination = Destination.withoutArgument()
internal val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()
internal val FreezeCardSectionDestination = Destination.withArgument<CardArgument>()

fun NavGraphBuilder.cardFeatureSections(
    navController: NavController,
    onNavigateToAccountSummary: () -> Unit, // example of cross-feature navigation
    onNavigateToAccountDetails: () -> Unit, // example of cross-feature navigation
) {
    cardsSummarySection(navController, CardsSummarySectionDestination, onNavigateToAccountSummary, onNavigateToAccountDetails)
    cardDetailsSection(navController, CardDetailsSectionDestination, onNavigateToAccountDetails)
    freezeCardSection(navController, FreezeCardSectionDestination)
}

/**
 * Cards summary section (aka cards list)
 */
fun NavGraphBuilder.cardsSummarySection(
    navController: NavController,
    sectionDestination: DestinationWithoutArgument,
    onNavigateToAccountSummary: () -> Unit, // example of cross-feature navigation
    onNavigateToAccountDetails: () -> Unit, // example of cross-feature navigation
) {
    composable(sectionDestination, "Cards Summary") {
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
private val CardDetailsDestination = Destination.withArgument<CardArgument>()
private val CancelCardDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.cardDetailsSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>,
    onNavigateToAccountDetails: () -> Unit, // example of cross-feature navigation
) {
    navigation(
        destination = sectionDestination,
        startDestination = CardDetailsDestination,
        label = "Card Details Section"
    ) {
        composable(CardDetailsDestination, "Card Details") { card ->
            CardDetails(
                card = card,
                onNavigateToAccountDetails = onNavigateToAccountDetails,
                onNavigateToFreezeCard = redirect(navController, FreezeCardSectionDestination),
                onNavigateToCancelCard = redirect(navController, CancelCardDestination)
            )
        }

        cancelCardSection(
            navController = navController,
            onNavigateToNextAfterCardCancelled = {
                navController.popBackStack(destination = sectionDestination, inclusive = true)
            },
            sectionDestination = CancelCardDestination
        )
    }
}

/**
 * Freeze card section
 */
private val FreezeCardWarningDestination = Destination.withArgument<CardArgument>()
private val FreezeCardConfirmationDestination = Destination.withArgument<CardArgument>()
internal fun NavGraphBuilder.freezeCardSection(
    navController: NavController,
    sectionDestination: DestinationWithRequiredArgument<CardArgument>,
) {
    navigation(
        destination = sectionDestination,
        startDestination = FreezeCardWarningDestination,
        label = "Freeze Card Section"
    ) {
        composable(FreezeCardWarningDestination, "Card Freeze Warning") { arg ->
            CardFreezeWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    FreezeCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(FreezeCardConfirmationDestination, "Card Freeze Confirmation") { arg ->
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
    sectionDestination: DestinationWithRequiredArgument<CardArgument>,
) {
    navigation(
        destination = sectionDestination,
        startDestination = CancelCardWarningDestination,
        label = "Cancel Card Section"
    ) {
        composable(CancelCardWarningDestination, "Card Cancellation Warning") { arg ->
            CardCancellationWarning(
                card = arg,
                onNavigateToConfirmation = redirect(
                    navController,
                    CancelCardConfirmationDestination
                ) // You should rather use Session instead of re-passing card argument, but this example focuses on navigation only.
            )
        }

        composable(CancelCardConfirmationDestination, "Card Cancellation Confirmation") { arg ->
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
    onNavigateToAccountDetails: (NavigateToAccountDetailsRequest) -> Unit,
) {
    CardsFeatureWidget(
        onNavigateToCardDetails = redirect(navController, CardDetailsSectionDestination),
        onNavigateToAccountDetails = { onNavigateToAccountDetails(NavigateToAccountDetailsRequest) }
    )
}
