package com.cyclone.solana.core.repository.interfaces

import com.cyclone.solana.core.datamodel.entity.KeyPair
import kotlinx.coroutines.flow.Flow

interface KeyPairRepository {
    suspend fun saveKeyPairs(vararg keyPair: KeyPair)

    suspend fun getKeyPair(publicKey: String): Flow<KeyPair?>

    suspend fun deleteKeyPairs(vararg keyPair: KeyPair)

    suspend fun getAllKeyPairs(): Flow<List<KeyPair>>

    suspend fun deleteAllKeyPairs()
}