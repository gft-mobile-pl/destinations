package com.gft.example.composenavigation.common.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.get
import androidx.navigation.navOptions

const val DESTINATION_ARGS_KEY = "destinationArgs"

typealias NavArgument = Parcelable

inline fun <reified T : NavArgument?> NavGraphBuilder.composable(
    @IdRes id: Int,
    redirects: List<Pair<Int, Int>> = emptyList(),
    crossinline content: @Composable (T?) -> Unit
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) { backStackEntry ->
                val args: T? =  backStackEntry.arguments?.getParcelable(DESTINATION_ARGS_KEY)
                content(args)
            }
            .apply {
                this.id = id
                for (redirect in redirects) {
                    this.putAction(redirect.first, redirect.second)
                }
                addArgument(
                    argumentName = DESTINATION_ARGS_KEY,
                    argument = NavArgumentBuilder()
                        .apply {
                            println("#Test null is T = ${null is T}")
                            nullable = (null is T)
                            type = NavType.ParcelableType(T::class.java)
                        }
                        .build())
            }
    )
}

fun NavController.navigate(@IdRes id: Int, argument: NavArgument?) {
    navigate(
        id,
        argument?.let {
            Bundle().apply { putParcelable(DESTINATION_ARGS_KEY, argument) }
        }
    )
}

fun NavController.navigate(@IdRes id: Int, argument: NavArgument?, builder: NavOptionsBuilder.() -> Unit) {
    navigate(
        id,
        argument?.let {
            Bundle().apply { putParcelable(DESTINATION_ARGS_KEY, argument) }
        },
        navOptions(builder)
    )
}