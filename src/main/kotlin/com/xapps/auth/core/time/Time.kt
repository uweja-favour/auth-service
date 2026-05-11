//@file:OptIn(ExperimentalTime::class)
//
//package com.xapps.auth.core.time
//
//import kotlinx.datetime.*
//import kotlinx.serialization.Serializable
//import kotlin.time.ExperimentalTime
//import kotlin.time.toKotlinInstant
//
//typealias KotlinInstant = kotlin.time.Instant
//typealias JavaInstant = java.time.Instant
//typealias KotlinLocalDate = kotlinx.datetime.LocalDate
//typealias KotlinLocalDateTime = kotlinx.datetime.LocalDateTime
//typealias KotlinDuration = kotlin.time.Duration
//
//// ----------------- Now helpers -----------------
//val nowInKotlinInstant: KotlinInstant
//    get() = kotlin.time.Clock.System.now()
//
//val nowInLocalDateTime: KotlinLocalDateTime
//    get() = nowInKotlinInstant.toLocalDateTime(TimeZone.currentSystemDefault())
//
//fun kotlinx.datetime.LocalDate.Companion.now(): KotlinLocalDate =
//    nowInKotlinInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
//
//fun kotlinInstantOfEpochMillis(l: Long): KotlinInstant =
//    KotlinInstant.fromEpochMilliseconds(l)
//
//fun kotlinx.datetime.LocalDateTime.Companion.now(): KotlinLocalDateTime =
//    nowInKotlinInstant.toLocalDateTime(TimeZone.UTC)
//
//fun kotlinx.datetime.LocalDateTime.isAfter(other: KotlinLocalDateTime): Boolean = this > other
//
//fun kotlin.time.Instant.isAfter(other: KotlinInstant): Boolean = this > other
//
//fun kotlin.time.Duration.Companion.between(v1: KotlinLocalDateTime, v2: KotlinLocalDateTime): KotlinDuration {
//    val startInstant = v1.toInstant(TimeZone.UTC)
//    val endInstant = v2.toInstant(TimeZone.UTC)
//    val secondsDifference = endInstant.epochSeconds - startInstant.epochSeconds
//    val nanosDifference = endInstant.nanosecondsOfSecond - startInstant.nanosecondsOfSecond
//    return (secondsDifference + nanosDifference / 1_000_000_000.0).seconds
//}
//
//fun java.time.Instant.toKotlinLocalDateTime(): KotlinLocalDateTime = this.toKotlinInstant().toLocalDateTime(TimeZone.UTC)
//fun kotlin.time.Instant.toKotlinLocalDateTime(): KotlinLocalDateTime = this.toLocalDateTime(TimeZone.UTC)
//
//fun java.util.Date.toKotlinInstant(): KotlinInstant = this.toInstant().toKotlinInstant()