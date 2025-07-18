package com.glamvibe.glamvibeadmin.utils

import android.content.Context
import kotlinx.datetime.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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

fun formatDate(date: LocalDate): String {
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.month.value.toString().padStart(2, '0')
    val year = date.year.toString()
    return "$day.$month.$year"
}

fun formatPromotionDate(date: LocalDate): String {
    val day = date.dayOfMonth.toString().padStart(2, '0')
    val month = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val year = date.year.toString()
    return "$day $month $year"
}