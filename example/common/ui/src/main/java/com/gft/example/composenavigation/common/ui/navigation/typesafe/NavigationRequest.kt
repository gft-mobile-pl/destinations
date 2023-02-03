package com.gft.example.composenavigation.common.ui.navigation.typesafe

import com.gft.example.composenavigation.common.ui.navigation.typesafe.Destination.DestinationProxy

fun interface OnNavigationRequest {

    fun NavigationRequestHandler.callback(navigationRequest: NavigationRequest<*>)

    operator fun invoke(destination: DestinationProxy<Unit>) = invoke(destination, Unit)

    operator fun <T : Any?> invoke(destination: DestinationProxy<T>, argument: T) {
        val navigationRequest = NavigationRequest(destination, argument)
        NavigationRequestHandler(navigationRequest).callback(navigationRequest)
    }

    class NavigationRequestHandler(
        private val navigationRequest: NavigationRequest<*>
    ) {
        fun <T : Any?> handle(destination: DestinationProxy<T>, handler: (argument: T) -> Unit) {
            if (navigationRequest.destination == destination) {
                @Suppress("UNCHECKED_CAST")
                handler(navigationRequest.argument as T)
            }
        }
    }

    data class NavigationRequest<T : Any?>(
        val destination: DestinationProxy<T>,
        val argument: T
    )
}