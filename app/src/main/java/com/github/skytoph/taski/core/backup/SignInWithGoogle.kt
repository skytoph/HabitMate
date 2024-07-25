package com.github.skytoph.taski.core.backup

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

interface SignInWithGoogle<C> {
    fun getClient(context: Context): C

    class DriveScope : SignInWithGoogle<GoogleSignInClient> {
        override fun getClient(context: Context): GoogleSignInClient {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_FILE))
                .build()

            return GoogleSignIn.getClient(context, signInOptions)
        }
    }
}