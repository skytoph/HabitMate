package com.github.skytoph.taski.di.core

import android.content.Context
import com.github.skytoph.taski.R
import com.github.skytoph.taski.core.backup.BackupDatastore
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BackupDatastoreModule {

    @Provides
    fun datastore(drive: Drive?): BackupDatastore = BackupDatastore.Base(drive)

    @Provides
    fun drive(context: Context): Drive? = GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
        val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))
        credential.selectedAccount = googleAccount.account

        Drive.Builder(NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName(context.getString(R.string.app_name))
            .build()
    }
}