package com.skytoph.taski.mock.data

import androidx.annotation.DrawableRes
import com.skytoph.taski.R
import com.skytoph.taski.presentation.habit.icon.IconsGroup

object HabitIconData {
    @DrawableRes
    fun getRandom(): Int = IconsGroup.unlocked.toList().subList(0, 6).random()

    @DrawableRes
    fun getLocked(): Int = R.drawable.gem
}

