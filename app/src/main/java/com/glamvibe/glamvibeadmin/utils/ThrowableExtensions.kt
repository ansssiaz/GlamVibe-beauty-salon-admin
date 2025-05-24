package com.glamvibe.glamvibeadmin.utils

import android.content.Context
import com.glamvibe.glamvibeadmin.R
import java.io.IOException

fun Throwable.getErrorText(context: Context): String = when (this) {
    is IOException -> context.getString(R.string.network_error)
    else -> context.getString(R.string.unknown_error)
}