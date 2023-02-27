package com.gft.example.composenavigation.login.ui.navigation

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.gft.destinations.Destination
import com.gft.destinations.Destination.DestinationWithoutArgument
import com.gft.destinations.composable
import com.gft.destinations.dialog
import com.gft.destinations.navigate
import com.gft.destinations.navigation
import com.gft.destinations.popBackStack
import com.gft.example.composenavigation.login.ui.screens.logout.LogoutDialog
import com.gft.example.composenavigation.login.ui.screens.otp.OtpScreen
import com.gft.example.composenavigation.login.ui.screens.usercredentials.UserCredentialsScreen

val LoginSectionDestination = Destination.withoutArgument()
private val CredentialsScreenDestination = Destination.withoutArgument()
private var OtpScreenDestination = Destination.withoutArgument()
fun NavGraphBuilder.loginSection(
    navController: NavHostController,
    sectionDestination: DestinationWithoutArgument = LoginSectionDestination,
    onNavigateToNextAfterSuccessfulLogin: () -> Unit
) {
    navigation(
        destination = sectionDestination,
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

val LogoutPromptSectionDestination = Destination.withoutArgument()
fun NavGraphBuilder.logoutPromptSection(
    navController: NavHostController,
    sectionDestination: DestinationWithoutArgument = LogoutPromptSectionDestination,
    onNavigateToNextAfterLogout: () -> Unit
) {
    dialog(
        sectionDestination,
        dialogProperties = DialogProperties(dismissOnClickOutside = false)
    ) {
        LogoutDialog(
            onLogoutClicked = onNavigateToNextAfterLogout,
            onCancelClicked = {
                navController.popBackStack(destination = sectionDestination, inclusive = true)
            })
    }
}