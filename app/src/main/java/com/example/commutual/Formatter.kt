package com.example.commutual

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class FormatterClass {
    companion object {

        fun formatTimestamp(timestamp: Timestamp, hasYear: Boolean): String {

            var timeFormatter = SimpleDateFormat(
                "HH:mm MMM d",
                Locale.getDefault()
            )

            if (hasYear) {
                timeFormatter = SimpleDateFormat(
                    "HH:mm:ss MMM d yyyy",
                    Locale.getDefault()
                )
            }

            return timeFormatter.format(timestamp.toDate())
        }

        fun formatDate(
            selectedYear: Int, selectedMonth: Int, selectedDay: Int,
        ): String {

            if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
                }

                val timeFormatter = SimpleDateFormat(
                    "MMM d yyyy",
                    Locale.getDefault()
                ).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }

                return timeFormatter.format(calendar.time)
            }

            return ""

        }


        fun formatTime(hour: Int, minute: Int, timeStyle24: Boolean): String {

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            var timeFormatter = SimpleDateFormat(
                "h:mm aa",
                Locale.getDefault()
            )

            if (timeStyle24) {
                timeFormatter = SimpleDateFormat(
                    "HH:mm",
                    Locale.getDefault()
                )
            }

            return timeFormatter.format(calendar.time)
        }

    }

}