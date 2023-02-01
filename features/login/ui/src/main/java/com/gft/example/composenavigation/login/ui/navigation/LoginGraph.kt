package com.gft.example.composenavigation.login.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.gft.example.composenavigation.common.ui.navigation.NavArgument
import com.gft.example.composenavigation.common.ui.navigation.RouteArgumentDefinitions
import com.gft.example.composenavigation.common.ui.navigation.RouteArguments
import com.gft.example.composenavigation.login.ui.screens.otp.OtpScreen
import com.gft.example.composenavigation.login.ui.screens.usercredentials.UserCredentialsScreen
import kotlinx.parcelize.Parcelize

private const val CREDENTIALS_SCREEN = "login/credentials"
private const val OTP_SCREEN = "login/otp"

@Parcelize
data class LoginGraphNavArgs(
    val firstName: String = "default args value of firstName",
    val lastName: String = "default args value of lastName"
) : Parcelable

/**
 * Option A (official).
 * Declare extension function to [NavGraphBuilder] which builds the internal [navigation].
 * The only deviation from the official documentation is that 'route' is passed as parameter not hardcoded.
 * This give the possibility to embed loginGraph multiplie times inside different graphs.
 *
 * Pros:
 * - NavHostController is reused
 * - internal navigation is not encapsulated (!) - one may navigate directly to any internal screen if correct route is used
 * Cons:
 * - NavGraphBuilder api is getting bigger and bigger with each extension function (all of the must be public!)
 * - one has to operate on side effects of extension functions rather that on Composables
 */
fun NavGraphBuilder.loginGraph(
    route: String,
    onNavigateToNextAfterSuccessfulLogin: () -> Unit,
    navController: NavHostController
) {
    navigation(startDestination = CREDENTIALS_SCREEN, route = route) {
        composable(CREDENTIALS_SCREEN) {
            UserCredentialsScreen(
                onNavigateToNext = { navController.navigate(OTP_SCREEN) }
            )
        }
        composable(OTP_SCREEN) {
            OtpScreen(
                onNavigateToNextAfterSuccessfulLogin = onNavigateToNextAfterSuccessfulLogin
            )
        }
    }
}

/**
 * Option B.
 * Fully encapsulates login related navigation inside its own graph.
 * Pros:
 * - internal navigation is fully encapsulated
 * - NavGraphBuilder api is kept tidy - there are no extension functions
 * - parent graph  simply embeds Composables instead of calling unrelated functions and relying on their side effects
 * Cons:
 * - new NavHost and new NavController is created for each graph
 */
@Composable
fun LoginGraph(
    modifier: Modifier = Modifier,
    onNavigateToNextAfterSuccessfulLogin: () -> Unit,
    loginGraphNavController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = loginGraphNavController,
        startDestination = CREDENTIALS_SCREEN
    ) {
        composable(CREDENTIALS_SCREEN) {
            UserCredentialsScreen(
                onNavigateToNext = { loginGraphNavController.navigate(OTP_SCREEN) }
            )
        }
        composable(OTP_SCREEN) {
            OtpScreen(
                onNavigateToNextAfterSuccessfulLogin = onNavigateToNextAfterSuccessfulLogin
            )
        }
    }
}