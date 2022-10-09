package com.cyclone.solana.core.repository.interfaces

import com.cyclone.solana.core.datamodel.entity.Seed
import kotlinx.coroutines.flow.Flow

interface SeedRepository {

    suspend fun saveSeeds(vararg seed: Seed)

    suspend fun getSeed(): Flow<Seed?>

    suspend fun getAllSeeds(): Flow<List<Seed>>

    suspend fun deleteSeeds(vararg seed: Seed)

    suspend fun deleteAllSeeds()
}