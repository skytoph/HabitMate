package com.github.skytoph.taski.di.habit

import com.github.skytoph.taski.core.Now
import com.github.skytoph.taski.presentation.core.ConvertIcon
import com.github.skytoph.taski.presentation.habit.create.IdCache
import com.github.skytoph.taski.presentation.habit.list.mapper.HabitDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object EditHabitViewModelModule {

    @Provides
    fun mapper(convertIcon: ConvertIcon, now: Now): HabitDomainMapper =
        HabitDomainMapper.Base(convertIcon, now)

    @Provides
    fun idCache(): IdCache = IdCache()
}