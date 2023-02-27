package com.gft.example.composenavigation.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gft.example.composenavigation.account.ui.navigation.AccountFeatureWidget
import com.gft.example.composenavigation.cards.ui.navigation.CardsFeatureWidget

object WidgetsFactory {
    @Composable
    fun CreateWidgets(
        navController: NavController,
        onNavigationRequest: (Any) -> Unit
    ) {
        CardsFeatureWidget(navController = navController, onNavigateToAccountDetails = onNavigationRequest)
        AccountFeatureWidget(navController = navController)
    }
}