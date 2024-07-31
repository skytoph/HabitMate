package com.github.skytoph.taski.presentation.core

interface MapResultToEvent<E> {
    fun apply(): E
}

interface MapResultToListOfEvents<E> {
    fun apply(): List<E>
}