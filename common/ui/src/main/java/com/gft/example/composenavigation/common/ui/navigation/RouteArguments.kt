package com.gft.example.composenavigation.common.ui.navigation

import android.net.Uri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import kotlin.reflect.KClass

/**
 * Holds definitions of route arguments along with assigned values.
 *
 * This class provides runtime type safety:
 * - if a type of the provided value does not match the type defined by corresponding argument definition an error is thrown,
 * - if any of the required values is not provided and there is no default value defined in the argument definition an error is thrown,
 * - if a value is provided for undefined argument an error is thrown.
 *
 * @param argumentDefinitions   Set of definitions of the arguments held by this class. By the definition this set should be defined as default
 *                              companion object of the concrete class inheriting from [RouteArguments].
 * @param arguments             Set of 'name/value' pairs defining values of corresponding arguments. By the convention this set should
 *                              map the names of the arguments to the corresponding class properties.
 *                              Note that it is not required to provide a value for optional arguments (that is the arguments which definitions
 *                              contain default value).
 */
abstract class RouteArguments(
    argumentDefinitions: RouteArgumentDefinitions,
    vararg arguments: Pair<String, *>
) {
    private val _query: String

    init {
        val remainingArgumentsDefinitions = argumentDefinitions.toMutableList()
        val queryBuilder = StringBuilder()

        for (argument in arguments) {
            val argumentDefinition = argumentDefinitions.get(argument.first)
                ?: throw IllegalArgumentException("Trying to set undefined argument '${argument.first}'.")

            if (argument.second != null) {
                val valueType = inferNavTypeFromValueClass(argument.second!!::class)
                if (valueType != argumentDefinition.argument.type)
                    throw IllegalArgumentException("Incompatible nav-type. Trying to set '${argument.second}' for the value of '${argument.first}'. Actual nav-type is $valueType while the required nav-type is ${argumentDefinition.argument.type}.")

                if (queryBuilder.isNotEmpty()) queryBuilder.append("&")
                queryBuilder.append(argumentDefinition.name)
                queryBuilder.append("=")
                queryBuilder.append(Uri.encode(argument.second.toString()))
            }

            remainingArgumentsDefinitions.remove(argumentDefinition)
        }

        for (argumentDefinition in remainingArgumentsDefinitions) {
            if (!argumentDefinition.argument.isDefaultValuePresent && !argumentDefinition.argument.isNullable) {
                throw IllegalArgumentException("No value provided for non-nullable '${argumentDefinition.name}' argument and there is no default value defined.")
            }
        }

        _query = queryBuilder.toString()
    }

    fun buildQuery(): String = _query
}

/**
 * Defines set of arguments that may be passed while navigating with 'routes'.
 * By the convention descendants of this class should be defined as default companion objects of the classes inheriting from [RouteArguments].
 *
 * Note that 'routes' support only subset of argument types supported by nav graphs.
 * Refer to https://developer.android.com/guide/navigation/navigation-pass-data.
 *
 * This class provides runtime type safety - if incompatible argument definition is provided an IllegalArgumentException is thrown.
 * This class also throws an error if an argument with the same name is defined more than once.
 */
abstract class RouteArgumentDefinitions(
    vararg argumentDefinitions: NamedNavArgument
) : List<NamedNavArgument> by argumentDefinitions.asList() {
    init {
        val argumentNames = mutableSetOf<String>()
        for (argumentDefinition in argumentDefinitions) {
            when (argumentDefinition.argument.type) {
                NavType.IntType,
                NavType.LongType,
                NavType.FloatType,
                NavType.BoolType,
                is NavType.SerializableType<*>,
                NavType.StringType -> {
                    // supported types
                }
                else -> throw IllegalArgumentException("Arguments of type ${argumentDefinition.argument.type} are not supported while using routes.")
            }
            if (argumentNames.contains(argumentDefinition.name)) {
                throw IllegalArgumentException("'${argumentDefinition.name}' argument is defined already.")
            }
            argumentNames.add(argumentDefinition.name)
        }
    }

    val queryTemplate = fold("") { query, argument ->
        val separator = if (query.isEmpty()) "" else "&"
        "$query$separator${argument.name}={${argument.name}}"
    }
}

inline fun <reified T : Any, R : T?> RouteArgumentDefinitions.resolveArgumentValue(argumentName: String, routeData: NavBackStackEntry): R {
    return resolveArgumentValue(argumentName, T::class, routeData)
}

fun <T : Any, R : T?> RouteArgumentDefinitions.resolveArgumentValue(argumentName: String, argumentClass: KClass<T>, routeData: NavBackStackEntry): R {
    val inferredType = inferNavTypeFromValueClass(argumentClass)
    val argumentDefinition = get(argumentName) ?: throw IllegalArgumentException("No definition found for '$argumentName}' argument.")

    if (inferredType != argumentDefinition.argument.type) {
        throw IllegalArgumentException("Incompatible nav-type. Trying to acquire $inferredType but the nav-type defined for '$argumentName' argument is ${argumentDefinition.argument.type}.")
    }

    if (routeData.arguments == null || !routeData.arguments!!.containsKey(argumentName)) {
        if (argumentDefinition.argument.isNullable || argumentDefinition.argument.defaultValue != null) {
            @Suppress("UNCHECKED_CAST")
            return argumentDefinition.argument.defaultValue as R
        }
        throw IllegalArgumentException("Provided NavBackStackEntry does not contain value for '$argumentName' argument and there is no default value defined.")
    }

    @Suppress("UNCHECKED_CAST")
    return when (argumentDefinition.argument.type) {
        NavType.IntType -> routeData.arguments?.getInt(argumentName)
        NavType.LongType -> routeData.arguments?.getLong(argumentName)
        NavType.FloatType -> routeData.arguments?.getFloat(argumentName)
        NavType.BoolType -> routeData.arguments?.getBoolean(argumentName)
        NavType.StringType -> routeData.arguments?.getString(argumentName)
        else -> throw IllegalArgumentException("")
    } as R
}

private fun List<NamedNavArgument>.get(argumentName: String) = firstOrNull { definition -> definition.name == argumentName }

private fun inferNavTypeFromValueClass(valueClass: KClass<*>): NavType<*> {
    return when (valueClass) {
        Int::class -> NavType.IntType
        Long::class -> NavType.LongType
        Float::class -> NavType.FloatType
        Boolean::class -> NavType.BoolType
        String::class -> NavType.StringType
        else -> throw IllegalArgumentException("Objects of type ${valueClass.java.name} are not supported for arguments while using routes.")
    }
}
