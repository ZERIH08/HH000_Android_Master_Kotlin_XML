package com.example.hh000_android_master_kotlin_xml.core.util

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MyTimeUtils {

    //----------------------------------------------------------------------------------------------
    fun getCurrentDateStringDDMMYYYY(): String {
        val sdf =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.ENGLISH
            )
        return sdf.format(Date())
    }

    fun getCurrentTimeString12HHMMAMPM(): String {
        val currentTime =
            LocalTime.now()
        val formatter =
            DateTimeFormatter.ofPattern(
                "hh:mm a",
                Locale.ENGLISH
            )
        return currentTime.format(formatter).uppercase()
    }

    //----------------------------------------------------------------------------------------------
    fun getTimeString12HHMMAMPMFrom24HourAndMinute(
        hour24: Int,
        minute: Int
    ): String {
        val hourOfDay =
            when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }
        val amPm =
            if (hour24 >= 12) "PM" else "AM"
        val hourString =
            hourOfDay.toString()
                .padStart(
                    2,
                    '0'
                )
        val minuteString =
            minute.toString()
                .padStart(
                    2,
                    '0'
                )

        return "$hourString:$minuteString $amPm"
    }

    fun get24HourIntFromTimeString12HHMMAMPM(timeString: String): Int {
        val format =
            SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            )
        val date =
            format.parse(timeString)
        val calendar =
            Calendar.getInstance().apply {
                if (date != null) {
                    time =
                        date
                }
            }
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinutesIntFromTimeString12HHMMAMPM(timeString: String): Int {
        val format =
            SimpleDateFormat(
                "hh:mm a",
                Locale.ENGLISH
            )
        val date =
            format.parse(timeString)
        val calendar =
            Calendar.getInstance().apply {
                if (date != null) {
                    time =
                        date
                }
            }
        return calendar.get(Calendar.MINUTE)
    }

    //----------------------------------------------------------------------------------------------
    fun getDateStringFYYYYMMDDHHMMSSFromMilliSecondsSinceEpoch(milliSecondsSinceTheEpoch: Long): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.ENGLISH
        ).format(
            Date(
                milliSecondsSinceTheEpoch
            )
        )
    }

    fun getDateStringFDDMMYYYYFromMilliSecondsSinceEpoch(milliSecondsSinceTheEpoch: Long): String {
        return SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.ENGLISH
        ).format(
            Date(
                milliSecondsSinceTheEpoch
            )
        )
    }

    fun getDateStringFMMMMDDYYYYFromMilliSecondsSinceEpoch(milliSecondsSinceTheEpoch: Long): String {
        return SimpleDateFormat(
            "MMM dd yyyy",
            Locale.ENGLISH
        ).format(
            Date(
                milliSecondsSinceTheEpoch
            )
        )
    }

    fun getTimeInMilliSecondsSinceEpochFromDateStringFDDMMYYYY(dateString: String): Long {
        val format =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.ENGLISH
            )
        val date =
            format.parse(dateString)
        return date?.time ?: 0L
    }
    //----------------------------------------------------------------------------------------------
}