package com.gft.example.composenavigation.common.ui.mvi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gft.example.composenavigation.common.livedata.ConsumableEventLiveData
import com.gft.example.composenavigation.common.livedata.ConsumableEventLiveData.ConsumableEventLiveDataDispatcher

abstract class BaseMviLiveDataViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect>
    : ViewModel(), MviLiveDataViewModel<VS, EV, NE, VE> {
    override val viewStates: LiveData<VS> = MutableLiveData()
    override val navigationEffects: ConsumableEventLiveData<NE> = ConsumableEventLiveDataDispatcher()
    override val viewEffects: ConsumableEventLiveData<VE> = ConsumableEventLiveDataDispatcher()
}

interface MviLiveDataViewModel<VS : ViewState, EV : ViewEvent, NE : NavigationEffect, VE : ViewEffect> {
    val viewStates: LiveData<VS>
    val navigationEffects: ConsumableEventLiveData<NE>
    val viewEffects: ConsumableEventLiveData<VE>
    fun onEvent(event: EV)

    fun MviLiveDataViewModel<VS, EV, NE, VE>.dispatchNavigationEffect(effect: NE) {
        if (navigationEffects is ConsumableEventLiveDataDispatcher) (navigationEffects as ConsumableEventLiveDataDispatcher).value = effect
        else throw TypeCastException("MviViewModel.dispatchNavigationEffect extension supports ConsumableEventDispatcher only!")
    }

    fun MviLiveDataViewModel<VS, EV, NE, VE>.dispatchViewEffect(effect: VE) {
        if (viewEffects is ConsumableEventLiveDataDispatcher) (viewEffects as ConsumableEventLiveDataDispatcher).value = effect
        else throw TypeCastException("MviViewModel.dispatchViewEffect extension supports ConsumableEventDispatcher only!")
    }

    var MviLiveDataViewModel<VS, EV, NE, VE>.viewState: VS
        get() {
            return viewStates.value
                ?: throw IllegalStateException("viewState was not set yet!")
        }
        set(value) {
            if (viewStates is MutableLiveData) (viewStates as MutableLiveData).value = value
            else throw TypeCastException("MviViewModel.viewState extension supports MutableLiveData only!")
        }
}