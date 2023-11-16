package com.github.skytoph.taski.di.auth

import android.content.Context
import com.github.skytoph.taski.presentation.auth.authentication.client.GoogleAuth
import com.github.skytoph.taski.presentation.auth.authentication.client.GoogleAuthUiClient
import com.github.skytoph.taski.presentation.auth.authentication.error.AuthErrorHandler
import com.github.skytoph.taski.presentation.auth.authentication.mapper.AuthErrorMapper
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthClientModule {

    @Provides
    @Singleton
    fun client(@ApplicationContext context: Context): GoogleAuth =
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(context),
            errorHandler = AuthErrorHandler(AuthErrorMapper()),
            auth = FirebaseAuth.getInstance()
        )
}