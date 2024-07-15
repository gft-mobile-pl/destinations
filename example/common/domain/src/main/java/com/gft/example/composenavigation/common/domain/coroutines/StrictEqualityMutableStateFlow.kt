package com.gft.example.composenavigation.common.domain.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Variation of [MutableStateFlow] which uses strict equality while comparing items.
 *
 * [MutableStateFlow] has two features which net effect may manifest in a bit unpredicted behaviour:
 * - it is conflated,
 * - it never emits two equal items one after another (works like [distinctUntilChanged]).
 *
 * In certain scenarios it is possible that client may not be notified about the mutation
 * of the [StateFlow], e.g. if the changes are very quick and the last modification resets
 * the flow to the initial value.
 * Consider the following scenario:
 * 1. initial value is "A",
 * 2. values "B", "A", "B", "A" are set very fast,
 * 3. client is very slow and the conflation takes place -> the flow is reduced to "A",
 * 4. initial value is equal to the end value ("A" and "A") -> no value is emitted.
 * However if a strict equality test is used to compare items the "A" would be emitted in the point 4.
 */
class StrictEqualityMutableStateFlow<T>(
    initialValue: T?,
) : MutableStateFlow<T?> {
    private val source = MutableStateFlow(StrictEqualityDataContainer<T?>(initialValue))
    override val subscriptionCount = source.subscriptionCount
    override suspend fun emit(value: T?) = source.emit(value.wrap())

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() = source.resetReplayCache()
    override fun tryEmit(value: T?) = source.tryEmit(value.wrap())
    override var value: T?
        get() = source.value.unwrap()
        set(value) {
            source.value = value.wrap()
        }

    override fun compareAndSet(expect: T?, update: T?) =
        source.compareAndSet(expect.wrap(), update.wrap())

    override val replayCache: List<T?> = source.replayCache.map { item -> item.unwrap() }
    override suspend fun collect(collector: FlowCollector<T?>) = source.collect { item ->
        collector.emit(item.unwrap())
    }

    private class StrictEqualityDataContainer<T>(private val data: T?) {
        override fun equals(other: Any?) =
            if (other is StrictEqualityDataContainer<*>) data === other.data else false

        override fun hashCode(): Int = data.hashCode()
        fun unwrap(): T? = data
    }

    private fun T?.wrap() = StrictEqualityDataContainer<T?>(this)
}

