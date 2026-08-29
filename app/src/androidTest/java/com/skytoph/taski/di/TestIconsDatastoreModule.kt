package com.skytoph.taski.di

import com.skytoph.taski.di.habit.IconsDatastoreModule
import com.skytoph.taski.mock.datastore.TestIconsDatastore
import com.skytoph.taski.presentation.habit.icon.IconsDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [IconsDatastoreModule::class]
)
object TestIconsDatastoreModule {

    @Provides
    @Singleton
    fun datastore(): IconsDatastore = TestIconsDatastore()
}