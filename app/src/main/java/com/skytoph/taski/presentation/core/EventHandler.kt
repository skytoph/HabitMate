package com.skytoph.taski.presentation.core

interface EventHandler<Event> {
    fun onEvent(event: Event)
}