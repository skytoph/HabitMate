package com.skytoph.taski.core

class LoopIterator<T>(private val data: List<T>) : ListIterator<T> {
    private var iterator: ListIterator<T> = data.listIterator()

    override fun hasNext(): Boolean = iterator.hasNext()

    override fun next(): T {
        if (!iterator.hasNext()) iterator = data.listIterator()
        return iterator.next()
    }

    override fun hasPrevious(): Boolean = iterator.hasPrevious()

    override fun nextIndex(): Int = iterator.nextIndex()

    override fun previous(): T {
        if (!iterator.hasPrevious()) iterator = data.listIterator(data.size)
        return iterator.previous()
    }

    override fun previousIndex(): Int = iterator.previousIndex()

    fun nextAndReturnBack(repeat: Int = 1): T {
        repeat(repeat - 1) { next() }
        val result = next()
        repeat(repeat) { previous() }
        return result
    }

    fun previousAndReturnBack(repeat: Int = 1): T {
        repeat(repeat - 1) { previous() }
        val result = previous()
        repeat(repeat) { next() }
        return result
    }
}