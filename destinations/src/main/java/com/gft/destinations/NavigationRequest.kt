package com.gft.destinations

import com.gft.destinations.Destination.DestinationProxy
import com.gft.destinations.OnNavigationRequest.NavigationRequestHandler

fun interface OnNavigationRequest {

    fun NavigationRequestHandler.callback(navigationRequest: NavigationRequest<*>)

    operator fun invoke(destination: DestinationProxy<Unit>) = invoke(destination, Unit)

    operator fun <T : Any?> invoke(destination: DestinationProxy<T>, argument: T) {
        val navigationRequest = NavigationRequest(destination, argument)
        DefaultNavigationRequestHandler(navigationRequest).callback(navigationRequest)
    }

    interface NavigationRequestHandler {
        fun <T : Any?> handle(destination: DestinationProxy<T>, handler: (argument: T) -> Unit)
    }

    data class NavigationRequest<T : Any?>(
        val destination: DestinationProxy<T>,
        val argument: T
    )
}

private class DefaultNavigationRequestHandler(
    private val navigationRequest: OnNavigationRequest.NavigationRequest<*>
) : NavigationRequestHandler {
    override fun <T : Any?> handle(destination: DestinationProxy<T>, handler: (argument: T) -> Unit) {
        if (navigationRequest.destination == destination) {
            @Suppress("UNCHECKED_CAST")
            handler(navigationRequest.argument as T)
        }
    }
}
