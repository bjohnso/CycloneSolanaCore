package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.database.CycloneDatabase
import com.cyclone.solana.core.datamodel.entity.KeyPair
import com.cyclone.solana.core.repository.interfaces.KeyPairRepository
import kotlinx.coroutines.flow.Flow

class KeyPairRepositoryImpl(private val database: CycloneDatabase): KeyPairRepository {
    override suspend fun saveKeyPairs(vararg keyPair: KeyPair) {
        val dao = database.keyPairDao()
        return dao.saveKeyPairs(
            *keyPair.toList().toTypedArray()
        )
    }

    override suspend fun getKeyPair(publicKey: String): Flow<KeyPair?> {
        val dao = database.keyPairDao()
        return dao.getKeyPair(publicKey)
    }

    override suspend fun deleteKeyPairs(vararg keyPair: KeyPair) {
        val dao = database.keyPairDao()
        return dao.deleteKeyPairs()
    }

    override suspend fun getAllKeyPairs(): Flow<List<KeyPair>> {
        val dao = database.keyPairDao()
        return dao.getAllKeyPairs()

    }

    override suspend fun deleteAllKeyPairs() {
        val dao = database.keyPairDao()
        return dao.deleteAllKeyPairs()
    }
}