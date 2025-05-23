package com.gft.example.composenavigation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.gft.destinations.Destination
import com.gft.destinations.NavHost
import com.gft.destinations.composable
import com.gft.destinations.log.log
import com.gft.destinations.popBackStack
import com.gft.destinations.redirect
import com.gft.example.composenavigation.login.ui.navigation.LoginSectionDestination
import com.gft.example.composenavigation.login.ui.navigation.loginSection
import com.gft.example.composenavigation.ui.screens.welcomescreen.WelcomeScreen

private val WelcomeScreenDestination = Destination.withoutArgument()

@Composable
fun ApplicationNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController().log {
        println("#Nav $it")
    },
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = WelcomeScreenDestination
    ) {
        composable(WelcomeScreenDestination, "Welcome") {
            WelcomeScreen(
                onNavigateToNext = redirect(navController, LoginSectionDestination)
            )
        }

        loginSection(
            onNavigateToNextAfterSuccessfulLogin = redirect(navController, LoggedInSectionDestination, navOptions {
                popUpTo(WelcomeScreenDestination.id) { inclusive = false }
            }),
            navController = navController
        )

        loggedInSection(
            onNavigateToNextAfterLogout = {
                navController.popBackStack(destination = WelcomeScreenDestination, inclusive = false)
            },
            navController = navController
        )
    }
}
