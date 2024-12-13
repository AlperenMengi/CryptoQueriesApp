package com.alperenmengi.cryptoqueriesapp.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yy", Locale.US)
        return try {
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else{
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }

} 