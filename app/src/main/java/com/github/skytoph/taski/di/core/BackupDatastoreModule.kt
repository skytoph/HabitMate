package com.github.skytoph.taski.di.core

import android.content.Context
import com.github.skytoph.taski.core.backup.BackupDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BackupDatastoreModule {

    @Provides
    fun datastore(@ApplicationContext context: Context): BackupDatastore = BackupDatastore.Base(context)
}