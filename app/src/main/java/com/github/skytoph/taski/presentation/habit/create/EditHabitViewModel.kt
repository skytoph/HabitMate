package com.github.skytoph.taski.presentation.habit.create

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.skytoph.taski.domain.habit.HabitRepository
import com.github.skytoph.taski.domain.habit.HabitToUiMapper
import com.github.skytoph.taski.presentation.habit.HabitScreens
import com.github.skytoph.taski.presentation.habit.HabitUi
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditHabitViewModel @Inject constructor(
    private val state: MutableState<EditHabitState> = mutableStateOf(EditHabitState()),
    private val repository: HabitRepository,
    private val mapper: HabitDomainMapper,
    private val mapperUi: HabitToUiMapper,
    private val idCache: IdCache,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        onEvent(EditHabitEvent.Progress(true))
        savedStateHandle.get<Long>(HabitScreens.EditHabit.habitIdArg)?.let { id ->
            if (id != HabitUi.ID_DEFAULT) {
                idCache.cache(id)
                viewModelScope.launch(Dispatchers.IO) {
                    val habit = repository.habit(id).map(mapperUi)
                    withContext(Dispatchers.Main) {
                        onEvent(EditHabitEvent.Init(habit))
                    }
                }
            } else
                onEvent(EditHabitEvent.Clear)
        }
    }

    fun saveHabit() = viewModelScope.launch(Dispatchers.IO) {
        val habit = state.value.toHabitUi(idCache.get()).map(mapper)
        repository.insert(habit)
    }

    fun onEvent(event: EditHabitEvent) = event.handle(state)

    fun state(): State<EditHabitState> = state

    fun isNewHabit(): Boolean = idCache.get() == HabitUi.ID_DEFAULT
}