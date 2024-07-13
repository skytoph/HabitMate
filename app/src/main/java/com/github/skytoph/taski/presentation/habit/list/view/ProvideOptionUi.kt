package com.github.skytoph.taski.presentation.habit.list.view

import com.github.skytoph.taski.core.Matches

interface ProvideOptionUi<T : Matches<T>> : Matches<T> {
    fun optionUi(): OptionUi
}