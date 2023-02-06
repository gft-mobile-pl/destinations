package com.gft.example.composenavigation.cards.ui.navigation

import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.gft.destinations.Destination
import kotlinx.parcelize.Parcelize

@Parcelize data class CardArgument(val cardId: String) : Parcelable
val CardDetailsSectionDestination = Destination.withArgument<CardArgument>()



fun NavGraphBuilder.cardDetailsSection(
    navController: NavController
) {

    freezeCardGraph(
        onNavigateToNextAfterCardFrozen = {}
    )
}

// fun NavGraphBuilder.freezeCardSection(
//     onNavigateToNextAfterCardFrozen: () -> Unit
// ) = freezeCardGraph(onNavigateToNextAfterCardFrozen)

internal fun cardDetailsGraph() {

}