package com.glamvibe.glamvibeadmin.utils

import android.content.Context

fun dpToPx(dp: Int, context: Context): Int {
    return (dp * context.resources.displayMetrics.density).toInt()
}

fun getYearWord(years: Int): String {
    val mod10 = years % 10
    val mod100 = years % 100

    return if (mod100 in 11..14) {
        "лет"
    } else {
        when (mod10) {
            1 -> "год"
            2, 3, 4 -> "года"
            else -> "лет"
        }
    }
}