package com.skytoph.taski.core

interface Matches<T> {
    fun matches(item: T): Boolean
}