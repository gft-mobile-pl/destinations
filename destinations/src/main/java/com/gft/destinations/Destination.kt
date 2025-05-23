package com.gft.destinations

import android.os.Parcelable
import java.io.Serializable

const val DESTINATION_ARGUMENT_KEY = "destinationArgumentKey"

private var lastId = 1000

sealed class Destination<T> private constructor() {
    val id: Int = ++lastId

    class DestinationWithoutArgument internal constructor() : Destination<Unit>()
    class DestinationWithOptionalArgument<T : Any> internal constructor() : Destination<T?>()
    class DestinationWithRequiredArgument<T : Any> internal constructor() : Destination<T>()

    companion object {
        fun withoutArgument() = DestinationWithoutArgument()
        fun <T : Parcelable> withArgument() = DestinationWithRequiredArgument<T>()
        fun <T : Parcelable> withOptionalArgument() = DestinationWithOptionalArgument<T>()

        /**
         * Always consider using Parcelable instead of Serializable in the first place.
         */
        fun <T : Serializable> withSerializableArgument() = DestinationWithRequiredArgument<T>()
        fun <T : Serializable> withOptionalSerializableArgument() = DestinationWithOptionalArgument<T>()

        /**
         * As a rule of thumb you should refrain from using Destinations with arguments of a primitive type
         * as the lack of name make them context-less and leave the developer with guessing what they mean.
         * Usually it is better to create a single-field parcelable class instead.
         */
        fun withStringArgument() = DestinationWithRequiredArgument<String>()
        fun withOptionalStringArgument() = DestinationWithOptionalArgument<String>()
        fun withIntArgument() = DestinationWithRequiredArgument<Int>()
        fun withOptionalIntArgument() = DestinationWithOptionalArgument<Int>()
        fun withLongArgument() = DestinationWithRequiredArgument<Long>()
        fun withOptionalLongArgument() = DestinationWithOptionalArgument<Long>()
        fun withFloatArgument() = DestinationWithRequiredArgument<Float>()
        fun withOptionalFloatArgument() = DestinationWithOptionalArgument<Float>()
        fun withBooleanArgument() = DestinationWithRequiredArgument<Boolean>()
        fun withOptionalBooleanArgument() = DestinationWithOptionalArgument<Boolean>()
    }
}
