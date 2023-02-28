package com.gft.example.composenavigation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.gft.destinations.Destination
import com.gft.destinations.NavHost
import com.gft.destinations.composable
import com.gft.destinations.popBackStack
import com.gft.destinations.redirect
import com.gft.example.composenavigation.login.ui.navigation.LoginSectionDestination
import com.gft.example.composenavigation.login.ui.navigation.loginSection
import com.gft.example.composenavigation.ui.screens.welcomescreen.WelcomeScreen

@Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val welcomeScreenDestination = Destination.withoutArgument()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = welcomeScreenDestination
    ) {
        composable(welcomeScreenDestination) {
            WelcomeScreen(
                onNavigateToNext = redirect(navController, LoginSectionDestination)
            )
        }

        loginSection(
            onNavigateToNextAfterSuccessfulLogin = redirect(navController, LoggedInSectionDestination, navOptions {
                popUpTo(welcomeScreenDestination.id) { inclusive = false }
            }),
            navController = navController
        )

        loggedInSection(
            onNavigateToNextAfterLogout = {
                navController.popBackStack(destination = welcomeScreenDestination, inclusive = false)
            },
            navController = navController
        )
    }
}
