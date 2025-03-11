package com.skytoph.taski.presentation.core

import com.google.firebase.FirebaseNetworkException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface NetworkErrorMapper {
    fun isNetworkUnavailable(exception: Exception): Boolean

    class Base : NetworkErrorMapper {
        override fun isNetworkUnavailable(exception: Exception): Boolean = when (exception) {
            is UnknownHostException -> true
            is SocketTimeoutException -> true
            is ConnectException -> true
            is FirebaseNetworkException -> true
            else -> false
        }
    }
}
