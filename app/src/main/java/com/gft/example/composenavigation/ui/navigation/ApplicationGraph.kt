package com.gft.example.composenavigation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gft.example.composenavigation.common.ui.navigation.Destination
import com.gft.example.composenavigation.common.ui.navigation.NavHost
import com.gft.example.composenavigation.common.ui.navigation.ProxyDestination
import com.gft.example.composenavigation.common.ui.navigation.composable
import com.gft.example.composenavigation.common.ui.navigation.navigate
import com.gft.example.composenavigation.common.ui.navigation.redirect
import com.gft.example.composenavigation.login.ui.navigation.LoginGraph
import com.gft.example.composenavigation.login.ui.navigation.LoginGraphNavArgs
import com.gft.example.composenavigation.ui.screens.welcomescreen.WelcomeScreen

private val WelcomeScreenDestination = Destination.withoutArgument("WelcomeScreen")
private val LoginGraphDestination = Destination.withArgument<LoginGraphNavArgs>("LoginGraph")
private val LoggedInGraph = Destination.withoutArgument("LoggedInGraph")

private val GoToLoginGraphLevel1 = ProxyDestination.withoutArgument()
private val GoToLoginGraphLevel2 = ProxyDestination.withArgument<LoginGraphNavArgs>()
private val GoToLoginGraphLevel3 = ProxyDestination.withoutArgument()


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
        composable(
            WelcomeScreenDestination,
            GoToLoginGraphLevel1.redirect(GoToLoginGraphLevel2, LoginGraphNavArgs(firstName = "redirect 1>2 firstName", lastName = "redirect 1>2 lastName")),
            GoToLoginGraphLevel2.redirect(LoginGraphDestination)
            //GoToLoginGraphLevel2.redirect(GoToLoginGraphLevel3),
            //GoToLoginGraphLevel3.redirect(LoginGraphDestination)
            //GoToLoginGraphLevel3.redirect(LoginGraphDestination, LoginGraphNavArgs(firstName = "redirect 3>d firstName", lastName = "redirect 3>d lastName") )
        ) {
            WelcomeScreen(
                onNavigateToNext = {
                    navController.navigate(
                        GoToLoginGraphLevel1
                    )
                }
            )
        }

        composable(LoginGraphDestination) {
            val test = remember {
                println("#Test LOGIN1 $it")
                5
            }

            LoginGraph(onNavigateToNextAfterSuccessfulLogin = { })
        }

        composable(LoggedInGraph) {

        }

        // OPTION A (check LoginGraph.kt for details)
        // loginGraph(
        //     route = LOGIN_GRAPH,
        //     navController = navController,
        //     onNavigateToNextAfterSuccessfulLogin = {
        //         navController.navigate(HOME_SCREEN) {
        //             popUpTo(WELCOME_SCREEN)
        //         }
        //     }
        // )
        //
        // // OPTION B (check LoginGraph.kt for details)
        // composable(LOGIN_GRAPH + "not_official") {
        //     LoginGraph(
        //         onNavigateToNextAfterSuccessfulLogin = {
        //             navController.navigate(HOME_SCREEN) {
        //                 popUpTo(WELCOME_SCREEN)
        //             }
        //         }
        //     )
        // }

        // composable(HOME_SCREEN) {
        //     HomeScreen(appNavController = navController)
        // }
    }
}