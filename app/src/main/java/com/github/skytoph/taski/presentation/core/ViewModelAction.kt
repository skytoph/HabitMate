package com.github.skytoph.taski.presentation.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ViewModelAction<T, E> {
    fun action(vararg beforeAction: E, doAction: suspend () -> T)

    class Base<T, E>(
        private val scope: CoroutineScope,
        private val eventHandler: EventHandler<E>,
        private val mapper: Mapper<T, E>
    ) : ViewModelAction<T, E> {

        override fun action(vararg beforeAction: E, doAction: suspend () -> T) =
            action(beforeAction = beforeAction, doAction = doAction, map = { mapper.map(it) })

        private fun action(vararg beforeAction: E, doAction: suspend () -> T, map: (T) -> List<E>) {
            beforeAction.forEach { event -> eventHandler.onEvent(event) }
            scope.launch(Dispatchers.IO) {
                val result = map(doAction())
                withContext(Dispatchers.Main) {
                    result.forEach { event -> eventHandler.onEvent(event) }
                }
            }
        }
    }

    fun interface EventHandler<E> {
        fun onEvent(event: E)
    }

    fun interface Mapper<T, E> {
        fun map(event: T): List<E>
    }
}