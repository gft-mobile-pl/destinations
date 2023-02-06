package com.gft.example.composenavigation.cards.ui.navigation

import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination

val FreezeCardDestination = Destination.withArgument<CardArgument>()

fun NavGraphBuilder.freezeCardSection(
    onNavigateToNextAfterCardFrozen: () -> Unit
) = freezeCardGraph(onNavigateToNextAfterCardFrozen)

internal fun freezeCardGraph(
    onNavigateToNextAfterCardFrozen: () -> Unit
) {

}