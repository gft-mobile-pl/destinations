package com.gft.destinations

import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun OnNavigationRequest(
    navController: NavController,
    callback: RedirectingNavigationRequestHandler.(OnNavigationRequest.NavigationRequest<*>) -> Unit
): OnNavigationRequest = RedirectingOnNavigationRequest(navController, callback)

class RedirectingNavigationRequestHandler(
    internal val navController: NavController,
    private val navigationRequest: OnNavigationRequest.NavigationRequest<*>
) {
    fun <T : Any?> handle(destination: Destination.DestinationProxy<T>, handler: (argument: T) -> Unit) {
        if (navigationRequest.destination == destination) {
            @Suppress("UNCHECKED_CAST")
            handler(navigationRequest.argument as T)
        }
    }
}

private class RedirectingOnNavigationRequest(
    private val navController: NavController,
    private val handlerCallback: RedirectingNavigationRequestHandler.(OnNavigationRequest.NavigationRequest<*>) -> Unit
) : OnNavigationRequest {
    override fun OnNavigationRequest.NavigationRequestHandler.callback(navigationRequest: OnNavigationRequest.NavigationRequest<*>) {
        RedirectingNavigationRequestHandler(navController, navigationRequest).handlerCallback(navigationRequest)
    }
}

// TODO: write redirections for all other Destination types

// normal handler
fun <T : Any> OnNavigationRequest.NavigationRequestHandler.redirect(
    navController: NavController,
    from: Destination.DestinationProxy<T?>,
    to: Destination.DestinationWithOptionalArgument<T>,
    navOptions: NavOptions? = null
) = handle(from) { navController.navigate(to, it, navOptions) }

// redirecting
fun <T : Any> RedirectingNavigationRequestHandler.redirect(
    from: Destination.DestinationProxy<T?>,
    to: Destination.DestinationWithOptionalArgument<T>,
    navOptions: NavOptions? = null
) = handle(from) { navController.navigate(to, it, navOptions) }