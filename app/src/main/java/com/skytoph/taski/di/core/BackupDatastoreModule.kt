package com.skytoph.taski.di.core

import android.content.Context
import com.skytoph.taski.core.backup.BackupDatastore
import com.skytoph.taski.presentation.core.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BackupDatastoreModule {

    @Provides
    fun datastore(@ApplicationContext context: Context, log: Logger): BackupDatastore =
        BackupDatastore.Base(context, log)
}