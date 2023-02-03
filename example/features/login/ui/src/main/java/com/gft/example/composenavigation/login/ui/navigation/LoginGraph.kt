package com.gft.example.composenavigation.login.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.example.composenavigation.common.ui.navigation.typesafe.Destination
import com.gft.example.composenavigation.common.ui.navigation.typesafe.composable
import com.gft.example.composenavigation.common.ui.navigation.typesafe.navigate
import com.gft.example.composenavigation.common.ui.navigation.typesafe.navigation
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
