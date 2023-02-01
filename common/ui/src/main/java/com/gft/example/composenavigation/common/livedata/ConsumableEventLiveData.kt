package com.gft.example.composenavigation.common.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

sealed class ConsumableEventLiveData<T> : LiveData<T>() {
    class ConsumableEventLiveDataDispatcher<T> : ConsumableEventLiveData<T>() {
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner) { valueToDispatch ->
                synchronized(this) {
                    value = null
                    if (valueToDispatch != null) observer.onChanged(valueToDispatch)
                }
            }
        }

        override fun observeForever(observer: Observer<in T>) {
            super.observeForever { valueToDispatch ->
                synchronized(this) {
                    value = null
                    if (valueToDispatch != null) observer.onChanged(valueToDispatch)
                }
            }
        }

        public override fun postValue(value: T?) {
            super.postValue(value)
        }

        public override fun setValue(value: T?) {
            super.setValue(value)
        }
    }
}