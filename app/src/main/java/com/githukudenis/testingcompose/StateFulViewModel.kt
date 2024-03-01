package com.githukudenis.testingcompose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class StateFulViewModel<T>(initial: T): ViewModel() {

    val _state: MutableStateFlow<T> = MutableStateFlow(initial)
    val state: StateFlow<T> get() = _state.asStateFlow()

    fun update(block: T.() -> T) {
        _state.update(block)
    }
}