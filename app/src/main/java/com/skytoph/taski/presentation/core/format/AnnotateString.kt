package com.skytoph.taski.presentation.core.format

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

fun <T : Any> annotate(
    initialString: String,
    arguments: Collection<T>,
    transform: (T) -> String,
    style: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
): AnnotatedString {
    val string = StringBuilder(initialString)
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()

    arguments.forEachIndexed { index, arg ->
        string.append(if (index > 0) ", " else " ")
        val argString = transform(arg)
        val startIndex = string.length
        string.append(argString)
        val endIndex = startIndex + argString.length
        spanStyles.add(
            AnnotatedString.Range(item = style, start = startIndex, end = endIndex)
        )
    }

    return AnnotatedString(text = string.toString(), spanStyles = spanStyles)
}

fun annotate(
    string: String,
    arguments: Collection<String>,
    style: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
): AnnotatedString {
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    var last = 0
    arguments.forEach { arg ->
        val startIndex = string.indexOf(arg, last)
        val endIndex = startIndex + arg.length
        last = endIndex
        spanStyles.add(AnnotatedString.Range(item = style, start = startIndex, end = endIndex))
    }
    return AnnotatedString(text = string, spanStyles = spanStyles)
}