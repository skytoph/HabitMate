package com.skytoph.taski.presentation.habit.icon

import android.content.res.Resources

object DefaultIconsDatastore {
    fun unlocked(resources: Resources): Set<String> =
        IconsGroup.unlocked.map { resources.getResourceEntryName(it) ?: "" }.toSet()
}