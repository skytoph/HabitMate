package com.github.skytoph.taski.presentation.core

interface EventHandler<Event> {
    fun onEvent(event: Event)
}