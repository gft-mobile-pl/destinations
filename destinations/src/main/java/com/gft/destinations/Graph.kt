package com.gft.destinations

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.createGraph
import androidx.navigation.get
import com.gft.destinations.Destination.DestinationWithoutArgument
import java.io.Serializable

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
            @Suppress("DEPRECATION")
            navController.createGraph(
                id = destination?.id ?: 0,
                startDestination = startDestination.id,
                builder = builder
            )
        },
        modifier
    )
}

inline fun NavGraphBuilder.composable(
    destination: DestinationWithoutArgument,
    crossinline content: @Composable () -> Unit
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) {
                content()
            }
            .apply {
                id = destination.id
            }
    )
}

inline fun <reified T : Any?> NavGraphBuilder.composable(
    destination: Destination<T>,
    crossinline content: @Composable (T) -> Unit
) {
    addDestination(
        ComposeNavigator
            .Destination(provider[ComposeNavigator::class]) { backStackEntry ->
                val argument: Any? = when {
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
                content(argument as T)
            }
            .apply {
                id = destination.id
                addArgument(
                    argumentName = DESTINATION_ARGUMENT_KEY,
                    argument = NavArgumentBuilder()
                        .apply {
                            nullable = null !is T
                            if (destination is Destination.DestinationWithDefaultArgument) {
                                defaultValue = destination.defaultArgument
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
                            type = NavType.ParcelableType(T::class.java)
                        }
                        .build())
            }
    )
}

inline fun NavGraphBuilder.navigation(
    destination: DestinationWithoutArgument,
    startDestination: Destination<*>,
    builder: NavGraphBuilder.() -> Unit
) {
    @Suppress("DEPRECATION")
    return destination(NavGraphBuilder(provider, destination.id, startDestination.id).apply(builder))
}