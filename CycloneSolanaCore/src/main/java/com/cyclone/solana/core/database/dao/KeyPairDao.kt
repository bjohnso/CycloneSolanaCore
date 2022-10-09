package com.cyclone.solana.core.database.dao

import androidx.room.*
import com.cyclone.solana.core.datamodel.entity.KeyPair
import kotlinx.coroutines.flow.Flow

@Dao
interface KeyPairDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveKeyPairs(vararg keyPair: KeyPair)

    @Transaction
    @Query("select * from tbl_keypairs where public_key = :publicKey limit 1")
    fun getKeyPair(publicKey: String): Flow<KeyPair?>

    @Transaction
    @Query("select * from tbl_keypairs")
    fun getAllKeyPairs(): Flow<List<KeyPair>>

    @Delete
    fun deleteKeyPairs(vararg keyPair: KeyPair)

    @Transaction
    @Query("delete from tbl_keypairs")
    fun deleteAllKeyPairs()
}