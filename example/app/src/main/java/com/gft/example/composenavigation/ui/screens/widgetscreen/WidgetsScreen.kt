package com.gft.example.composenavigation.ui.screens.widgetscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gft.example.composenavigation.ui.utils.WidgetsFactory

@Composable
fun WidgetsScreen(
    onNavigationRequest: (Any) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        /**
         * The production-ready application should rather work like this:
         * - there is a use-case that decides which actions are possible for the user at the moment
         * - VM resolves which widgets can be used (basically only the widgets providing allowed actions)
         * - VM provides in the state identifiers of the widgets that should be created
         * - WidgetsFactory creates the request widgets.
         */
        WidgetsFactory.CreateWidgets(
            navController = navController,
            onNavigationRequest = onNavigationRequest
        )
    }
}
