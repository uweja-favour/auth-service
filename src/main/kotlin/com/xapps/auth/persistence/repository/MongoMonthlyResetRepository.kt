package com.xapps.auth.persistence.repository

import com.xapps.auth.persistence.entity.MonthlyResetDocument
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MongoMonthlyResetRepository :
    CoroutineCrudRepository<MonthlyResetDocument, String> {

    suspend fun findTopByOrderByIdAsc(): MonthlyResetDocument?
}