package com.github.skytoph.taski.presentation.habit

abstract class HabitScreens(val route: String) {

    object Profile : HabitScreens("profile")

    object HabitList : HabitScreens("habits")

    object CreateHabit : HabitScreens("create_habit")

    class EditHabit(habitId: String) : HabitScreens("$screenRoute?$habitIdArg=$habitId") {

        companion object {
            const val habitIdArg = "habitId"
            const val screenRoute = "edit_habit"
            const val baseRoute = "$screenRoute?$habitIdArg={$habitIdArg}"
        }
    }

    object SelectIcon : HabitScreens("select_icon")
}