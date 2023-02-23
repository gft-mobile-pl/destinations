package com.gft.destinations

import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * DestinationWithoutArgument
 */
fun redirect(
    navController: NavController,
    destination: Destination.DestinationWithoutArgument,
    navOptions: NavOptions? = null
): () -> Unit = { navController.navigate(destination, navOptions) }

fun redirectIgnoreArgument(
    navController: NavController,
    destination: Destination.DestinationWithoutArgument,
    navOptions: NavOptions? = null
): (Any?) -> Unit = { navController.navigate(destination, navOptions) }

/**
 * DestinationWithOptionalArgument
 */
fun <T : Any> redirect(
    navController: NavController,
    destination: Destination.DestinationWithOptionalArgument<T>,
    navOptions: NavOptions? = null
): (T?) -> Unit = { arg: T? -> navController.navigate(destination, arg, navOptions) }

fun <T : Any, U> redirect(
    navController: NavController,
    destination: Destination.DestinationWithOptionalArgument<T>,
    navOptions: NavOptions? = null,
    argumentMapper: (U) -> T?
): (U) -> Unit = { argument -> navController.navigate(destination, argumentMapper(argument), navOptions) }

fun <T : Any> redirectWithArgument(
    navController: NavController,
    destination: Destination.DestinationWithOptionalArgument<T>,
    argument: T?,
    navOptions: NavOptions? = null
): () -> Unit = { navController.navigate(destination, argument, navOptions) }

/**
 * DestinationWithRequiredArgument
 */
fun <T : Any> redirect(
    navController: NavController,
    destination: Destination.DestinationWithRequiredArgument<T>,
    navOptions: NavOptions? = null
): (T) -> Unit = { arg: T -> navController.navigate(destination, arg, navOptions) }

fun <T : Any, U> redirect(
    navController: NavController,
    destination: Destination.DestinationWithRequiredArgument<T>,
    navOptions: NavOptions? = null,
    argumentMapper: (U) -> T
): (U) -> Unit = { argument -> navController.navigate(destination, argumentMapper(argument), navOptions) }

fun <T : Any> redirectWithArgument(
    navController: NavController,
    destination: Destination.DestinationWithRequiredArgument<T>,
    argument: T,
    navOptions: NavOptions? = null
): () -> Unit = { navController.navigate(destination, argument, navOptions) }
