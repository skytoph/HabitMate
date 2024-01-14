package com.github.skytoph.taski.core

interface MutableCache<T> {
    fun cache(value: T)
    fun get(): T
}