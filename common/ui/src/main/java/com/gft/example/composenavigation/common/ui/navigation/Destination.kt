package com.gft.example.composenavigation.common.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavAction
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.createGraph
import androidx.navigation.get
import androidx.navigation.navOptions
import com.gft.example.composenavigation.common.ui.navigation.Destination.DestinationWithRequiredArgument
import com.gft.example.composenavigation.common.ui.navigation.Destination.DestinationWithDefaultArgument
import com.gft.example.composenavigation.common.ui.navigation.Destination.DestinationWithOptionalArgument
import com.gft.example.composenavigation.common.ui.navigation.Destination.DestinationWithoutArgument
import com.gft.example.composenavigation.common.ui.navigation.ProxyDestination.ProxyDestinationWithArgument
import com.gft.example.composenavigation.common.ui.navigation.ProxyDestination.ProxyDestinationWithOptionalArgument
import com.gft.example.composenavigation.common.ui.navigation.ProxyDestination.ProxyDestinationWithoutArgument
import com.gft.example.composenavigation.common.ui.navigation.Redirection.ArgumentMode

const val DESTINATION_ARGUMENT_KEY = "destinationArgumentKey"
const val DESTINATION_ARGUMENT_MODE_KEY = "destinationArgumentModeKey"

private var lastId = 1000

sealed class Destination<T : Any?> private constructor(
    val defaultArgument: T,
    debugName: String = ""
) {
    val id: Int = ++lastId

    init {
        println("#Test Destination created: $id / $debugName")
    }

    class DestinationWithoutArgument internal constructor(debugName: String) : Destination<Nothing?>(null, debugName)
    class DestinationWithOptionalArgument<T : Parcelable> internal constructor(debugName: String) : Destination<T?>(null, debugName)
    class DestinationWithRequiredArgument<T : Parcelable> internal constructor(debugName: String) : Destination<T?>(null, debugName)
    class DestinationWithDefaultArgument<T : Parcelable> internal constructor(defaultArgument: T, debugName: String) :
        Destination<T>(defaultArgument, debugName)

    companion object {
        fun withoutArgument(debugName: String = "") = DestinationWithoutArgument(debugName)
        fun <T : Parcelable> withArgument(defaultArgument: T, debugName: String = "") = DestinationWithDefaultArgument(defaultArgument, debugName)
        fun <T : Parcelable> withArgument(debugName: String = "") = DestinationWithRequiredArgument<T>(debugName)
        fun <T : Parcelable> withOptionalArgument(debugName: String = "") = DestinationWithOptionalArgument<T>(debugName)
    }
}

sealed class ProxyDestination<T : Any?> {
    val id: Int = ++lastId

    class ProxyDestinationWithoutArgument internal constructor() : ProxyDestination<Unit>()
    class ProxyDestinationWithArgument<T : Parcelable> internal constructor() : ProxyDestination<T>()
    class ProxyDestinationWithOptionalArgument<T : Parcelable> internal constructor() : ProxyDestination<T?>()

    companion object {
        fun withoutArgument() = ProxyDestinationWithoutArgument()
        fun <T : Parcelable> withArgument() = ProxyDestinationWithArgument<T>()
        fun <T : Parcelable> withOptionalArgument() = ProxyDestinationWithOptionalArgument<T>()
    }

    override fun toString(): String {
        return "NavigationRequest(id=$id)"
    }
}

/**
 * #####################################################
 * Redirections
 * #####################################################
 */
class Redirection<T : Any?> internal constructor(
    val fromId: Int,
    val toId: Int,
    val argument: T,
    val argumentMode: ArgumentMode,
    val navOptions: NavOptions? = null
) {
    init {
        println("#Test Redirection created: $fromId / $toId / $argument / $argumentMode")
    }

    enum class ArgumentMode {
        OVERRIDE,
        USE_IF_MISSING
    }
}

/**
 * NavigationRequest -> NavigationRequest
 */
fun ProxyDestinationWithoutArgument.redirect(
    target: ProxyDestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: ProxyDestinationWithArgument<T>,
    argument: T
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: ProxyDestinationWithOptionalArgument<T>,
    argument: T? = null
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun ProxyDestinationWithOptionalArgument<*>.redirect(
    target: ProxyDestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: ProxyDestinationWithOptionalArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: ProxyDestinationWithOptionalArgument<T>,
    argument: T? = null,
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: ProxyDestinationWithArgument<T>,
    argument: T,
    argumentMode: ArgumentMode
) = Redirection(this.id, target.id, argument, argumentMode)

fun ProxyDestinationWithArgument<*>.redirect(
    target: ProxyDestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: ProxyDestinationWithOptionalArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: ProxyDestinationWithOptionalArgument<T>,
    argument: T?
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: ProxyDestinationWithArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: ProxyDestinationWithArgument<T>,
    argument: T
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

/**
 * NavigationRequest -> Destination
 */
fun ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithOptionalArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithOptionalArgument<T>,
    argument: T
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithDefaultArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithDefaultArgument<T>,
    argument: T
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithoutArgument.redirect(
    target: DestinationWithRequiredArgument<T>,
    argument: T
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithOptionalArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithOptionalArgument<T>,
    argument: T?
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithDefaultArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithDefaultArgument<T>,
    argument: T?
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithOptionalArgument<T>.redirect(
    target: DestinationWithRequiredArgument<T>,
    argument: T,
    argumentMode: ArgumentMode
) = Redirection(this.id, target.id, argument, argumentMode)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithoutArgument
) = Redirection(this.id, target.id, null, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithOptionalArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithOptionalArgument<T>,
    argument: T?
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithDefaultArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithDefaultArgument<T>,
    argument: T?
) = Redirection(this.id, target.id, argument, ArgumentMode.OVERRIDE)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithRequiredArgument<T>
) = Redirection(this.id, target.id, null, ArgumentMode.USE_IF_MISSING)

fun <T : Parcelable> ProxyDestinationWithArgument<T>.redirect(
    target: DestinationWithRequiredArgument<T>,
    overrideArgument: T
) = Redirection(this.id, target.id, overrideArgument, ArgumentMode.OVERRIDE)

/**
 * #####################################################
 * Adding composables.
 * #####################################################
 */
inline fun NavGraphBuilder.composable(
    destination: Destination<Nothing?>,
    vararg redirections: Redirection<out Any?>,
    crossinline content: @Composable () -> Unit
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) {
                content()
            }
            .apply {
                id = destination.id
                addRedirections(redirections)
            }
    )
}

inline fun <reified T : Parcelable?> NavGraphBuilder.composable(
    destination: Destination<T>,
    vararg redirections: Redirection<out Any>,
    crossinline content: @Composable (T) -> Unit
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) { backStackEntry ->
                content(backStackEntry.arguments?.getParcelable<T>(DESTINATION_ARGUMENT_KEY) as T)
            }
            .apply {
                id = destination.id
                addArgument(
                    argumentName = DESTINATION_ARGUMENT_KEY,
                    argument = NavArgumentBuilder()
                        .apply {
                            nullable = null !is T
                            if (destination.defaultArgument != null) defaultValue = destination.defaultArgument
                            type = NavType.ParcelableType(T::class.java)
                        }
                        .build())
                addRedirections(redirections)
            }
    )
}

fun ComposeNavigator.Destination.addRedirections(redirections: Array<out Redirection<out Any?>>) {
    for (redirection in redirections) {
        putAction(
            actionId = redirection.fromId,
            NavAction(
                destinationId = redirection.toId,
                navOptions = redirection.navOptions,
                defaultArguments = Bundle().apply {
                    if (redirection.argument != null) {
                        putParcelable(DESTINATION_ARGUMENT_KEY, (redirection.argument) as Parcelable)
                    }
                    putInt(DESTINATION_ARGUMENT_MODE_KEY, redirection.argumentMode.ordinal)
                }
            )
        )
    }
}

/**
 * #####################################################
 * Navigation with NavigationRequest
 * #####################################################
 */

fun NavController.navigate(
    navigationRequest: ProxyDestinationWithoutArgument
) {
    navigateWithNavigationRequest(navigationRequest, null)
}

private fun <T : Parcelable> NavController.navigate(
    navigationRequest: ProxyDestinationWithOptionalArgument<T>,
    argument: T? = null
) {
    navigateWithNavigationRequest(navigationRequest, argument)
}

private fun <T : Parcelable> NavController.navigate(
    navigationRequest: ProxyDestinationWithArgument<T>,
    argument: T
) {
    navigateWithNavigationRequest(navigationRequest, argument)
}

private fun <T : Parcelable?> NavController.navigateWithNavigationRequest(
    proxyDestination: ProxyDestination<*>,
    argument: T?
) {
    var action: NavAction? =
        currentDestination?.getAction(proxyDestination.id) ?: throw IllegalStateException("Missing redirection for $proxyDestination.")
    var finalDestinationId: Int = -1
    var finalArgument: Bundle? = argument?.let { Bundle().apply { putParcelable(DESTINATION_ARGUMENT_KEY, it) } }

    while (action != null) {
        val argumentMode = ArgumentMode.values()[action.defaultArguments?.getInt(DESTINATION_ARGUMENT_MODE_KEY)
            ?: throw IllegalStateException("Missing argument mode in redirection for $finalDestinationId")]
        val actionArgument = if (action.defaultArguments?.containsKey(DESTINATION_ARGUMENT_KEY) == true) action.defaultArguments else null

        finalDestinationId = action.destinationId
        finalArgument = if (finalArgument == null || argumentMode == ArgumentMode.OVERRIDE) actionArgument else finalArgument
        action = currentDestination?.getAction(action.destinationId)
    }

    this.navigate(finalDestinationId, finalArgument)
    navigate(finalDestinationId, finalArgument)
}

inline fun <reified T : Any> NavController.navigate(
    destination: Destination<T>,
    argument: T? = null
) {
    if (Unit is T) {
        navigate(destination.id)
    } else {
        navigate(
            destination.id,
            Bundle().apply {
                putParcelable(DESTINATION_ARGUMENT_KEY, (argument ?: destination.defaultArgument) as Parcelable)
            }
        )
    }
}

inline fun <reified T : Any> NavController.navigate(
    destination: Destination<T>,
    argument: T? = null,
    noinline builder: NavOptionsBuilder.() -> Unit
) {
    if (Unit is T) {
        navigate(destination.id, null, builder)
    } else {
        navigate(
            destination.id,
            Bundle().apply {
                putParcelable(DESTINATION_ARGUMENT_KEY, (argument ?: destination.defaultArgument) as Parcelable)
            },
            navOptions(builder)
        )
    }
}

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination<out Any?>,
    modifier: Modifier = Modifier,
    destination: Destination<out Any>? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    androidx.navigation.compose.NavHost(
        navController,
        remember(destination, startDestination, builder) {
            navController.createGraph(
                id = destination?.id ?: 0,
                startDestination = startDestination.id,
                builder = builder
            )
        },
        modifier
    )
}
