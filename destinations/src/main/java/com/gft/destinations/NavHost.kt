package com.gft.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.createGraph

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination<out Any?>,
    modifier: Modifier = Modifier,
    destination: Destination<out Any>? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    androidx.navigation.compose.NavHost(
        navController,
        remember(destination, startDestination, builder) {
            @Suppress("DEPRECATION")
            navController.createGraph(
                id = destination?.id ?: 0,
                startDestination = startDestination.id,
                builder = builder
            )
        },
        modifier
    )
}
