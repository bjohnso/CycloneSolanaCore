package com.cyclone.solana.core.database.dao

import androidx.room.*
import com.cyclone.solana.core.datamodel.entity.KeyPair

@Dao
interface KeyPairDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun persistKeyPairs(vararg keyPair: KeyPair)

    @Transaction
    @Query("select * from tbl_keypairs where public_key = :publicKey limit 1")
    fun retrieveKeyPair(publicKey: String): KeyPair?

    @Transaction
    @Query("select * from tbl_keypairs")
    fun retrieveAllKeyPairs(): List<KeyPair>

    @Delete
    fun destroyKeyPairs(vararg keyPair: KeyPair)

    @Transaction
    @Query("delete from tbl_keypairs")
    fun destroyAllKeyPairs()
}