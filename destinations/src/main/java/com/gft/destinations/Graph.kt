package com.gft.destinations

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.get
import com.gft.destinations.Destination.DestinationWithOptionalArgument
import com.gft.destinations.Destination.DestinationWithRequiredArgument
import com.gft.destinations.Destination.DestinationWithoutArgument
import java.io.Serializable

/**
 * Composable nodes.
 */
inline fun NavGraphBuilder.composable(
    destination: DestinationWithoutArgument,
    label: String? = null,
    crossinline content: @Composable () -> Unit,
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) {
                content()
            }
            .apply {
                id = destination.id
                this.label = label
            }
    )
}

inline fun <reified T : Any> NavGraphBuilder.composable(
    destination: DestinationWithOptionalArgument<T>,
    label: String? = null,
    crossinline content: @Composable (T?) -> Unit,
) = composable(destination.id, null, label, content)

inline fun <reified T : Any> NavGraphBuilder.composable(
    destination: DestinationWithOptionalArgument<T>,
    defaultArgument: T,
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) = composable(destination.id, defaultArgument, label, content)

inline fun <reified T : Any> NavGraphBuilder.composable(
    destination: DestinationWithRequiredArgument<T>,
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) = composable(destination.id, null, label, content)

@PublishedApi
internal inline fun <reified T : Any?> NavGraphBuilder.composable(
    destinationId: Int,
    defaultArgument: T?,
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) { backStackEntry ->
                content(extractArgument<T>(backStackEntry) as T)
            }
            .apply {
                id = destinationId
                this.label = label
                addArgument(
                    argumentName = DESTINATION_ARGUMENT_KEY,
                    argument = NavArgumentBuilder().apply(defineDestinationNavArgument<T>(defaultArgument)).build()
                )
            }
    )
}

/**
 * Dialog nodes.
 */
inline fun NavGraphBuilder.dialog(
    destination: DestinationWithoutArgument,
    dialogProperties: DialogProperties = DialogProperties(),
    label: String? = null,
    crossinline content: @Composable () -> Unit,
) {
    addDestination(
        DialogNavigator
            .Destination(provider[DialogNavigator::class], dialogProperties) {
                content()
            }
            .apply {
                id = destination.id
                this.label = label
            }
    )
}

inline fun <reified T : Any> NavGraphBuilder.dialog(
    destination: DestinationWithOptionalArgument<T>,
    dialogProperties: DialogProperties = DialogProperties(),
    label: String? = null,
    crossinline content: @Composable (T?) -> Unit,
) = dialog(destination.id, null, dialogProperties, label, content)

inline fun <reified T : Any> NavGraphBuilder.dialog(
    destination: DestinationWithOptionalArgument<T>,
    defaultArgument: T,
    dialogProperties: DialogProperties = DialogProperties(),
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) = dialog(destination.id, defaultArgument, dialogProperties, label, content)

inline fun <reified T : Any> NavGraphBuilder.dialog(
    destination: DestinationWithRequiredArgument<T>,
    dialogProperties: DialogProperties = DialogProperties(),
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) = dialog(destination.id, null, dialogProperties, label, content)

@PublishedApi
internal inline fun <reified T : Any?> NavGraphBuilder.dialog(
    destinationId: Int,
    defaultArgument: T?,
    dialogProperties: DialogProperties,
    label: String? = null,
    crossinline content: @Composable (T) -> Unit,
) {
    addDestination(
        DialogNavigator
            .Destination(provider[DialogNavigator::class], dialogProperties) { backStackEntry ->
                content(extractArgument<T>(backStackEntry) as T)
            }
            .apply {
                id = destinationId
                this.label = label
                addArgument(
                    argumentName = DESTINATION_ARGUMENT_KEY,
                    argument = NavArgumentBuilder().apply(defineDestinationNavArgument<T>(defaultArgument)).build()
                )
            }
    )
}

/**
 * Navigation nodes.
 */
fun NavGraphBuilder.navigation(
    destination: Destination<*>,
    startDestination: DestinationWithoutArgument,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id).apply {
            this.label = label
            builder()
        }
    )
}

inline fun <reified T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithoutArgument,
    startDestination: DestinationWithOptionalArgument<T>,
    defaultArgument: T? = null,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id)
            .apply {
                this.label = label
                argument(
                    name = DESTINATION_ARGUMENT_KEY,
                    argumentBuilder = defineDestinationNavArgument<T?>(defaultArgument)
                )
                builder()
            }
    )
}

inline fun <reified T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithoutArgument,
    startDestination: DestinationWithRequiredArgument<T>,
    defaultArgument: T,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id)
            .apply {
                this.label = label
                argument(
                    name = DESTINATION_ARGUMENT_KEY,
                    argumentBuilder = defineDestinationNavArgument(defaultArgument)
                )
                builder()
            }
    )
}

inline fun <reified T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithOptionalArgument<T>,
    startDestination: DestinationWithOptionalArgument<T>,
    defaultArgument: T? = null,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id)
            .apply {
                this.label = label
                argument(
                    name = DESTINATION_ARGUMENT_KEY,
                    argumentBuilder = defineDestinationNavArgument<T?>(defaultArgument)
                )
                builder()
            }
    )
}

inline fun <reified T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithOptionalArgument<T>,
    startDestination: DestinationWithRequiredArgument<T>,
    defaultArgument: T,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id)
            .apply {
                this.label = label
                argument(
                    name = DESTINATION_ARGUMENT_KEY,
                    argumentBuilder = defineDestinationNavArgument(defaultArgument)
                )
                builder()
            }
    )
}

fun <T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithRequiredArgument<T>,
    startDestination: DestinationWithOptionalArgument<T>,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id).apply {
            this.label = label
            builder()
        }
    )
}

fun <T : Any> NavGraphBuilder.navigation(
    destination: DestinationWithRequiredArgument<T>,
    startDestination: DestinationWithRequiredArgument<T>,
    label: String? = null,
    builder: NavGraphBuilder.() -> Unit,
) {
    @Suppress("DEPRECATION")
    return destination(
        NavGraphBuilder(provider, destination.id, startDestination.id).apply {
            this.label = label
            builder()
        }
    )
}

/**
 * NavBackStackEntry utilities.
 */
@PublishedApi
internal inline fun <reified T : Any?> extractArgument(backStackEntry: NavBackStackEntry): Any? = when {
    T::class == Int::class -> backStackEntry.arguments?.getInt(DESTINATION_ARGUMENT_KEY)
    T::class == Long::class -> backStackEntry.arguments?.getLong(DESTINATION_ARGUMENT_KEY)
    T::class == Float::class -> backStackEntry.arguments?.getFloat(DESTINATION_ARGUMENT_KEY)
    T::class == Boolean::class -> backStackEntry.arguments?.getBoolean(DESTINATION_ARGUMENT_KEY)
    T::class == String::class -> backStackEntry.arguments?.getString(DESTINATION_ARGUMENT_KEY)
    Parcelable::class.java.isAssignableFrom(T::class.java) -> {
        @Suppress("DEPRECATION")
        backStackEntry.arguments?.getParcelable(DESTINATION_ARGUMENT_KEY) as Parcelable?
    }

    Serializable::class.java.isAssignableFrom(T::class.java) -> {
        @Suppress("DEPRECATION")
        backStackEntry.arguments?.getSerializable(DESTINATION_ARGUMENT_KEY)
    }

    else -> throw IllegalArgumentException("Arguments of type ${T::class.java.name} is not supported by Destination.")
}

/**
 * NavArgument utilities.
 */
@PublishedApi
internal inline fun <reified T : Any?> defineDestinationNavArgument(
    defaultArgument: T?,
): NavArgumentBuilder.() -> Unit = {
    nullable = null is T
    if (defaultArgument != null) {
        defaultValue = defaultArgument
    }
    type = when {
        T::class == Int::class -> NavType.IntType
        T::class == Long::class -> NavType.LongType
        T::class == Float::class -> NavType.FloatType
        T::class == Boolean::class -> NavType.BoolType
        T::class == String::class -> NavType.StringType
        Parcelable::class.java.isAssignableFrom(T::class.java) -> {
            NavType.ParcelableType(T::class.java)
        }

        Serializable::class.java.isAssignableFrom(T::class.java) -> {
            @Suppress("UNCHECKED_CAST")
            NavType.SerializableType(T::class.java as Class<Serializable>)
        }

        else -> throw IllegalArgumentException("Arguments of type ${T::class.java.name} is not supported by Destination.")
    }
}
