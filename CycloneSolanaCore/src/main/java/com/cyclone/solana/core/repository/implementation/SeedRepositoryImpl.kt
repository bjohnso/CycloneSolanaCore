package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.database.CycloneDatabase
import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.repository.interfaces.SeedRepository
import kotlinx.coroutines.flow.Flow

class SeedRepositoryImpl(private val database: CycloneDatabase): SeedRepository {
    override suspend fun saveSeeds(vararg seed: Seed) {
        val dao = database.seedDao()
        return dao.saveSeeds(*seed.toList().toTypedArray())
    }

    override suspend fun getSeed(): Flow<Seed?> {
        val dao = database.seedDao()
        return dao.getSeed()
    }

    override suspend fun getAllSeeds(): Flow<List<Seed>> {
        val dao = database.seedDao()
        return dao.getSeedAllSeeds()
    }

    override suspend fun deleteSeeds(vararg seed: Seed) {
        val dao = database.seedDao()
        return dao.deleteSeeds(*seed.toList().toTypedArray())
    }

    override suspend fun deleteAllSeeds() {
        val dao = database.seedDao()
        return dao.deleteAllSeeds()
    }
}