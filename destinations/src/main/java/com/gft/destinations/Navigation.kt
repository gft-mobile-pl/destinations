package com.gft.destinations

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.gft.destinations.Destination.DestinationWithDefaultArgument
import com.gft.destinations.Destination.DestinationWithOptionalArgument
import com.gft.destinations.Destination.DestinationWithRequiredArgument
import com.gft.destinations.Destination.DestinationWithoutArgument
import java.io.Serializable

fun NavController.navigate(
    destination: DestinationWithoutArgument,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, null, navOptions)

fun <T : Any> NavController.navigate(
    destination: DestinationWithOptionalArgument<T>,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, null, navOptions)

fun <T : Any> NavController.navigate(
    destination: DestinationWithOptionalArgument<T>,
    argument: T?,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, argument, navOptions)

fun <T : Any> NavController.navigate(
    destination: DestinationWithDefaultArgument<T>,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, null, navOptions)

fun <T : Any> NavController.navigate(
    destination: DestinationWithDefaultArgument<T>,
    argument: T?,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, argument, navOptions)

fun <T : Any> NavController.navigate(
    destination: DestinationWithRequiredArgument<T>,
    argument: T,
    navOptions: NavOptions? = null
) = navigateToDestination(destination, argument, navOptions)

fun NavController.navigate(
    destination: DestinationWithoutArgument,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, null, navOptions(navOptionsBuilder))

fun <T : Any> NavController.navigate(
    destination: DestinationWithOptionalArgument<T>,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, null, navOptions(navOptionsBuilder))

fun <T : Any> NavController.navigate(
    destination: DestinationWithOptionalArgument<T>,
    argument: T?,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, argument, navOptions(navOptionsBuilder))

fun <T : Any> NavController.navigate(
    destination: DestinationWithDefaultArgument<T>,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, null, navOptions(navOptionsBuilder))

fun <T : Any> NavController.navigate(
    destination: DestinationWithDefaultArgument<T>,
    argument: T?,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, argument, navOptions(navOptionsBuilder))

fun <T : Any> NavController.navigate(
    destination: DestinationWithRequiredArgument<T>,
    argument: T,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit
) = navigateToDestination(destination, argument, navOptions(navOptionsBuilder))

private fun <T : Any?> NavController.navigateToDestination(
    destination: Destination<T>,
    argument: T?,
    navOptions: NavOptions?
) {
    if (argument != null) {
        navigate(
            destination.id,
            Bundle().apply {
                when (argument) {
                    is String -> putString(DESTINATION_ARGUMENT_KEY, argument)
                    is Int -> putInt(DESTINATION_ARGUMENT_KEY, argument)
                    is Long -> putLong(DESTINATION_ARGUMENT_KEY, argument)
                    is Float -> putFloat(DESTINATION_ARGUMENT_KEY, argument)
                    is Boolean -> putBoolean(DESTINATION_ARGUMENT_KEY, argument)
                    is Parcelable -> putParcelable(DESTINATION_ARGUMENT_KEY, argument)
                    is Serializable -> putSerializable(DESTINATION_ARGUMENT_KEY, argument)
                    else -> throw IllegalArgumentException("Not supported argument type!")
                }
            },
            navOptions
        )
    } else {
        navigate(destination.id, null, navOptions)
    }
}
