package com.xapps.auth.persistence.repository

interface MongoUsedGooglePlayPurchaseTokenCustomRepository {

    suspend fun deactivateAllByUserId(userId: String): Long
}