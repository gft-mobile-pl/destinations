package com.gft.example.composenavigation.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gft.destinations.Destination
import com.gft.destinations.NavHost
import com.gft.destinations.composable
import com.gft.destinations.navigate
import com.gft.destinations.redirectIgnoreArgument
import com.gft.example.composenavigation.login.ui.navigation.loginGraph
import com.gft.example.composenavigation.login.ui.screens.otp.OtpScreen
import com.gft.example.composenavigation.ui.screens.welcomescreen.WelcomeScreen
import kotlinx.parcelize.Parcelize

private val WelcomeScreenDestination = Destination.withoutArgument()

@Parcelize
data class TestDestinationArgs(
    val name: String
) : Parcelable

data class TestSerializableArgs(
    val name: String
) : java.io.Serializable

//private val TestDestination = Destination.withArgument<TestDestinationArgs>()
private val TestDestination = Destination.withSerializableArgument<TestSerializableArgs>()



@Composable
fun ApplicationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = WelcomeScreenDestination
    ) {
        composable(WelcomeScreenDestination) {
            WelcomeScreen(
                onNavigateToNextNoArgs = {
                    //navController.navigate(TestDestination, TestDestinationArgs(name = "Gucio"))
                    //navController.navigate(TestDestination, "Gucio")
                    navController.navigate(TestDestination, TestSerializableArgs("some value"))
                },
                onNavigateToNextWithString = redirectIgnoreArgument(navController, WelcomeScreenDestination)
            )
        }

        composable(TestDestination) {
            println("#Test args are: $it")
            OtpScreen(onSuccessfulLogin = {})
        }

        loginGraph(
            onNavigateToNextAfterSuccessfulLogin = {

            },
            navController = navController
        )
    }
}
