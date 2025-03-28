package com.skytoph.taski.presentation.settings.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.skytoph.taski.BuildConfig
import com.skytoph.taski.R

fun feedbackIntent(
    context: Context,
    text: String,
    subject: String = context.getString(R.string.feedback_subject),
    feedbackEmail: String = context.getString(R.string.feedback_email)
): Intent = Intent(Intent.ACTION_SENDTO).apply {
    val encodedSubject = Uri.encode(subject)
    val encodedBody = Uri.encode(text)
    val uri = context.getString(R.string.feedback_uri, feedbackEmail, encodedSubject, encodedBody)
    data = Uri.parse(uri)
    putExtra(Intent.EXTRA_EMAIL, arrayOf(feedbackEmail))
}

fun feedbackTemplate(context: Context, supportId: String): String =
    context.getString(
        R.string.feedback_text,
        context.getString(R.string.app_name),
        BuildConfig.VERSION_NAME,
        BuildConfig.VERSION_CODE,
        supportId
    )

fun sendFeedback(context: Context, uid: String) {
    val shareIntent = Intent.createChooser(feedbackIntent(context, feedbackTemplate(context, uid)), null)
    try {
        context.startActivity(shareIntent)
    } catch (exception: Exception) {
        Toast.makeText(context, context.getString(R.string.error_failed_to_find_email_app), Toast.LENGTH_LONG).show()
    }
}