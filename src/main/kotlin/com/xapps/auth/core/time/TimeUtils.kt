//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.core.time
//
//import kotlinx.datetime.DateTimeUnit
//import kotlinx.datetime.Instant
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.LocalDateTime
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.isoDayNumber
//import kotlinx.datetime.minus
//import kotlinx.datetime.number
//import kotlinx.datetime.toInstant
//import kotlinx.datetime.toLocalDateTime
//import kotlin.time.Duration.Companion.hours
//import kotlin.time.ExperimentalTime
//
///**
// * Returns the current date formatted as M/d/yyyy (US locale)
// * Example: 4/28/2025
// */
//fun getFormattedDate(timeProvider: TimeProvider = SystemTimeProvider()): String {
//    val currentTimeMillis = timeProvider.currentTimeMillis()
//    val instant = Instant.fromEpochMilliseconds(currentTimeMillis)
//    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
//    return "${localDate.monthNumber}/${localDate.dayOfMonth}/${localDate.year}"
//}
//
///**
// * Formats epoch milliseconds with ordinal suffix
// * Example: 12th of Dec, 2024
// */
//fun formatTimestampWithOrdinal(timestampMillis: Long, zone: TimeZone = TimeZone.currentSystemDefault()): String {
//    val localDate = Instant.fromEpochMilliseconds(timestampMillis).toLocalDateTime(zone).date
//    val day = localDate.dayOfMonth
//    val month = localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
//    val year = localDate.year
//    val suffix = getDayOrdinalSuffix(day)
//    return "${day}${suffix} of $month, $year"
//}
//
//
///** checks if a date String which is in the format: Sept 10, 2025 12:17 am is older than
// * 6 hours ago
// */
//
//
//fun isOlderThan24Hours(dateStr: String, zone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
//    return try {
//        // Parse the string manually
//        // Example format: "Sep 10, 2025 12:17 am"
//        val regex = Regex("""([A-Za-z]{3}) (\d{1,2}), (\d{4}) (\d{1,2}):(\d{2}) (am|pm)""")
//        val match = regex.matchEntire(dateStr) ?: return false
//
//        val (monthStr, dayStr, yearStr, hourStr, minuteStr, amPm) = match.destructured
//
//        val month = when (monthStr.lowercase()) {
//            "jan" -> 1; "feb" -> 2; "mar" -> 3; "apr" -> 4
//            "may" -> 5; "jun" -> 6; "jul" -> 7; "aug" -> 8
//            "sep" -> 9; "oct" -> 10; "nov" -> 11; "dec" -> 12
//            else -> return false
//        }
//
//        var hour = hourStr.toInt()
//        val minute = minuteStr.toInt()
//
//        // Convert 12-hour to 24-hour
//        hour = when {
//            amPm.lowercase() == "am" && hour == 12 -> 0
//            amPm.lowercase() == "pm" && hour < 12 -> hour + 12
//            else -> hour
//        }
//
//        val parsedDateTime = LocalDateTime(yearStr.toInt(), month, dayStr.toInt(), hour, minute)
//        val parsedInstant = parsedDateTime.toInstant(zone)
//
//        val twentyFourHoursAgo = nowInKotlinInstant - 24.hours
//
//        parsedInstant < twentyFourHoursAgo
//    } catch (e: Exception) {
//        false
//    }
//}
//
//
///**
// * Formats epoch milliseconds with ordinal suffix
// * Example: 12th of Dec, 2024
// */
//fun formatTimestampWithOrdinal(timestampMillis: KotlinInstant, zone: TimeZone = TimeZone.currentSystemDefault()): String {
//    val localDate = Instant.fromEpochMilliseconds(timestampMillis.toEpochMilliseconds()).toLocalDateTime(zone).date
//    val day = localDate.dayOfMonth
//    val month = localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
//    val year = localDate.year
//    val suffix = getDayOrdinalSuffix(day)
//    return "${day}${suffix} of $month, $year"
//}
//
///**
// * Determines the correct ordinal suffix for a given day.
// *
// * Examples:
// * - 1 → "st"
// * - 2 → "nd"
// * - 3 → "rd"
// * - 4 → "th"
// * - 11, 12, 13 → "th" (special case)
// *
// * @param day Day of the month (1–31).
// * @return Ordinal suffix for the given day.
// */
//fun getDayOrdinalSuffix(day: Int): String = when {
//    day in 11..13 -> "th"
//    day % 10 == 1 -> "st"
//    day % 10 == 2 -> "nd"
//    day % 10 == 3 -> "rd"
//    else -> "th"
//}
//
///**
// * Converts epoch milliseconds to LocalDate in the specified time zone
// */
//fun Long.toLocalDate(zone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
//    return Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date
//}
//
///**
// * Converts a LocalDateTime to epoch millis (Long) in UTC.
// */
//fun LocalDateTime.toEpochMillis(): Long {
//    return this.toInstant(TimeZone.UTC).toEpochMilliseconds()
//}
//
///**
// * Converts epoch millis (Long) to LocalDateTime in UTC.
// */
//fun Long.toLocalDateTime(): LocalDateTime {
//    return KotlinInstant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
//}
//
//
///**
// * Formats LocalDateTime in the pattern: MMM dd, yyyy h:mm a
// * Example: Sept 10, 2025 12:17 am
// */
//fun formatLocalDateTime(dateTime: LocalDateTime, zone: TimeZone = TimeZone.currentSystemDefault()): String {
//    val monthStr = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
//    val day = dateTime.dayOfMonth
//    val year = dateTime.year
//    var hour = dateTime.hour
//    val minute = dateTime.minute
//    val amPm = if (hour >= 12) "pm" else "am"
//    hour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
//    return "$monthStr $day, $year $hour:${minute.toString().padStart(2, '0')} $amPm"
//}
//
//
///**
// * Checks if a formatted date string (MMM dd, yyyy h:mm a) is older than 24 hours
// */
//fun isOlderThan24HoursKmp(dateTime: LocalDateTime, zone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
//    val now = nowInKotlinInstant
//    val twentyFourHoursAgo = now.minus(24 * 60 * 60 * 1000, DateTimeUnit.MILLISECOND)
//    val dateInstant = dateTime.toInstant(zone)
//    return dateInstant < twentyFourHoursAgo
//}
//
///**
// * Converts a duration in seconds into a human-readable format with days, hours, minutes, and seconds.
// * If the duration is null, a default text is returned. If duration is zero or less, "0 seconds" is returned.
// *
// * Example:
// *   Input: 93784 seconds (1 day, 2 hours, 3 minutes, 4 seconds)
// *   Output: "1 day 2 hours 3 minutes 4 seconds"
// *
// *   Input: null
// *   Output: "No time limit" (default text)
// *
// * @param seconds The duration in seconds, or null.
// * @param defaultText The text to return if seconds is null. Default is "No time limit".
// * @return A human-readable duration string.
// */
//fun formatToCleanDuration(seconds: Int?, defaultText: String = "No time limit"): String {
//    if (seconds == null) return defaultText
//    if (seconds <= 0) return "0 seconds"
//
//    val days = seconds / 86400
//    val hours = (seconds % 86400) / 3600
//    val minutes = (seconds % 3600) / 60
//    val secs = seconds % 60
//
//    val parts = mutableListOf<String>()
//
//    if (days > 0) parts.add("$days " + if (days == 1) "day" else "days")
//    if (hours > 0) parts.add("$hours " + if (hours == 1) "hour" else "hours")
//    if (minutes > 0) parts.add("$minutes " + if (minutes == 1) "minute" else "minutes")
//    if (secs > 0) parts.add("$secs " + if (secs == 1) "second" else "seconds")
//
//    return parts.joinToString(" ")
//}
//
//
//fun getKotlinDateTimeString(dateTime: LocalDateTime): String {
//    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
//    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
//
//    val isoDayIndex = dateTime.date.dayOfWeek.isoDayNumber % 7  // 1..7 -> 0..6
//    val dayOfWeek = days[isoDayIndex]
//
//    val month = months[dateTime.month.number - 1]
//    val day = dateTime.day
//
//    val hour24 = dateTime.hour
//    val minute = dateTime.minute
//    val ampm = if (hour24 < 12) "AM" else "PM"
//    val hour12 = ((hour24 + 11) % 12) + 1
//
//    // ✅ Kotlin way: no String.format
//    val minutePadded = minute.toString().padStart(2, '0')
//    return "$dayOfWeek, $month $day at $hour12:$minutePadded $ampm"
//}
//
