package com.gft.example.composenavigation.login.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.destinations.Destination
import com.gft.destinations.composable
import com.gft.destinations.navigate
import com.gft.destinations.navigation
import com.gft.example.composenavigation.login.ui.screens.otp.OtpScreen
import com.gft.example.composenavigation.login.ui.screens.usercredentials.UserCredentialsScreen

val LoginGraphDestination = Destination.withoutArgument()
private val CredentialsScreenDestination = Destination.withoutArgument()
private var OtpScreenDestination = Destination.withoutArgument()

fun NavGraphBuilder.loginGraph(
    onNavigateToNextAfterSuccessfulLogin: () -> Unit,
    navController: NavHostController
) {
    navigation(
        destination = LoginGraphDestination,
        startDestination = CredentialsScreenDestination
    ) {
        composable(CredentialsScreenDestination) {
            UserCredentialsScreen(
                onNavigateToOtp = { navController.navigate(OtpScreenDestination) }
            )
        }
        composable(OtpScreenDestination) {
            OtpScreen(
                onSuccessfulLogin = onNavigateToNextAfterSuccessfulLogin
            )
        }
    }
}
