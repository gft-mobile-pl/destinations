package com.gft.destinations.log

import android.annotation.SuppressLint
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination

private val navControllerBackQueueField = NavController::class.java.getDeclaredField("backQueue").apply {
    isAccessible = true
}

fun NavDestination.resolveLabel() = when (navigatorName) {
    "navigation" -> "[${label ?: "Navigation"}]"
    "dialog" -> "*${label ?: "Dialog"}"
    else -> "${label ?: "Screen"}"
}

@SuppressLint("RestrictedApi")
fun <NavControllerType : NavController> NavControllerType.log(onLogLine: (String) -> Unit) = this.apply {
    addOnDestinationChangedListener { _, destination, _ ->
        onLogLine("Current destination: ${destination.resolveLabel()}(${destination.id})")
        onLogLine(
            extractBackQueue()
                .map { it.destination }
                .fold("") { message, entry ->
                    "$message${entry.resolveLabel()}(${entry.id}) >> "
                }
                .removeSuffix(" >> ")
                .replaceFirst("Navigation", "Root")
        )
    }
}

@Suppress("UNCHECKED_CAST")
fun NavController.extractBackQueue() = navControllerBackQueueField.get(this) as List<NavBackStackEntry>
