package com.xapps.auth.persistence.repository.impl

import com.xapps.auth.domain.model.MonthlyReset
import com.xapps.auth.persistence.entity.MonthlyResetDocument
import com.xapps.auth.persistence.repository.MongoMonthlyResetRepository
import com.xapps.auth.persistence.saveUpserting
import org.springframework.stereotype.Repository

interface MonthlyResetRepository {

    suspend fun get(): MonthlyReset?

    suspend fun insert(reset: MonthlyReset): MonthlyReset

    suspend fun update(reset: MonthlyReset): MonthlyReset
}

@Repository
class MonthlyResetRepositoryImpl(
    private val mongoRepository: MongoMonthlyResetRepository
) : MonthlyResetRepository {

    override suspend fun get(): MonthlyReset? {
        return mongoRepository.findTopByOrderByIdAsc()
            ?.toDomain()
    }

    override suspend fun insert(reset: MonthlyReset): MonthlyReset {
        mongoRepository.saveUpserting(reset.toDocument())
        return reset
    }

    override suspend fun update(reset: MonthlyReset): MonthlyReset {
        mongoRepository.saveUpserting(reset.toDocument())
        return reset
    }

    private fun MonthlyReset.toDocument(): MonthlyResetDocument {
        return MonthlyResetDocument(
            id1 = id,
            lastReset = lastReset
        )
    }

    private fun MonthlyResetDocument.toDomain(): MonthlyReset {
        return MonthlyReset(
            id = id1,
            lastReset = lastReset
        )
    }
}