package com.skytoph.taski.presentation.habit.list.view

import com.skytoph.taski.core.Matches

interface ProvideOptionUi<T : Matches<T>> : Matches<T> {
    fun optionUi(): OptionUi
}