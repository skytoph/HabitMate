package com.github.skytoph.taski.presentation.habit

abstract class HabitScreens(val route: String) {

    object HabitList : HabitScreens("habits") {
        const val keyDelete = "delete"
        const val keyArchive = "archive"
    }

    object CreateHabit : HabitScreens("create_habit")

    class EditHabit(habitId: String) : HabitScreens("$screenRoute?$habitIdArg=$habitId") {

        companion object {
            const val habitIdArg = "habitId"
            const val screenRoute = "edit_habit"
            const val baseRoute = "$screenRoute?$habitIdArg={$habitIdArg}"
        }
    }

    class HabitDetails(habitId: String) : HabitScreens("$screenRoute?$habitIdArg=$habitId") {

        companion object {
            const val habitIdArg = "habitId"
            const val screenRoute = "habit_details"
            const val baseRoute = "$screenRoute?$habitIdArg={$habitIdArg}"
        }
    }

    object SelectIcon : HabitScreens("select_icon")

    object ReorderHabits : HabitScreens("reorder_habits")
}