package com.example.newsapp

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("SimpleDateFormat")
fun convertTimestampToMinutes(timestamp: String): Int {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        val date = sdf.parse(timestamp)
        val currentTime = Date()
        val diffMillis = abs(currentTime.time - date.time)
        (diffMillis / (60 * 1000).toDouble()).roundToInt()
    } catch (e: ParseException) {
        // Handle parsing exception
        0
    }
}

fun main() {
    val time = "2024-01-23T17:58:51Z"
    val minutes = convertTimestampToMinutes(time)
    println("Minutes from current time: $minutes")
}

