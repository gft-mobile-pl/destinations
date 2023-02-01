package com.gft.example.composenavigation.common.ui.mvi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.gft.example.composenavigation.common.domain.data.ConsumableEvent

private const val VIEW_STATE_KEY = "MviViewMode.viewState"

@OptIn(SavedStateHandleSaveableApi::class)
abstract class BaseMviViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect>(
    initialState: VS,
    savedStateHandle: SavedStateHandle? = null
) : ViewModel(), MviViewModel<VS, EV, NE, VE> {
    override val viewStates: State<VS> = savedStateHandle?.saveable(VIEW_STATE_KEY) { mutableStateOf(initialState) } ?: mutableStateOf(initialState)
    override val viewEffects: State<ConsumableEvent<VE>?> = mutableStateOf<ConsumableEvent<VE>?>(null)
    override val navigationEffects: State<ConsumableEvent<NE>?> = mutableStateOf<ConsumableEvent<NE>?>(null)
}

interface MviViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect> {
    val viewStates: State<VS>
    val viewEffects: State<ConsumableEvent<VE>?>
    val navigationEffects: State<ConsumableEvent<NE>?>
    fun onEvent(event: EV)

    fun MviViewModel<VS, EV, NE, VE>.dispatchNavigationEffect(effect: NE) {
        if (navigationEffects is MutableState) (navigationEffects as MutableState).value = ConsumableEvent(effect)
        else throw TypeCastException("MviViewModel.dispatchNavigationEffect extension supports MutableState only!")
    }

    fun MviViewModel<VS, EV, NE, VE>.dispatchViewEffect(effect: VE) {
        if (viewEffects is MutableState) (viewEffects as MutableState).value = ConsumableEvent(effect)
        else throw TypeCastException("MviViewModel.dispatchViewEffect extension supports MutableState only!")
    }

    var MviViewModel<VS, EV, NE, VE>.viewState: VS
        get() {
            return viewStates.value
        }
        set(value) {
            if (viewStates is MutableState) (viewStates as MutableState).value = value
            else throw TypeCastException("MviViewModel.viewState extension supports MutableState only!")
        }
}
