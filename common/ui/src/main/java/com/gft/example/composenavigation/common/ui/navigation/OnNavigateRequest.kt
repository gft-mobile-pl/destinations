package com.gft.example.composenavigation.common.ui.navigation

typealias OnDirectNavigateRequest<T> = (destinationIdentifier: T, arguments: RouteArguments?) -> Unit
typealias OnNavigateRequest = (arguments: RouteArguments?) -> Unit