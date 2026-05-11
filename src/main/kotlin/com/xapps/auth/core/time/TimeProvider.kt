//package com.xapps.auth.core.time
//
//import kotlin.time.ExperimentalTime
//
//
//interface TimeProvider {
//    fun currentTimeMillis(): Long
//}
//
//// Default implementation using System.currentTimeMillis()
//class SystemTimeProvider : TimeProvider {
//    @OptIn(ExperimentalTime::class)
//    override fun currentTimeMillis(): Long = nowInKotlinInstant.toEpochMilliseconds()
//}