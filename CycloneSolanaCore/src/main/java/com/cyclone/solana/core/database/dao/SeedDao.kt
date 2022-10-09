package com.cyclone.solana.core.database.dao

import androidx.room.*
import com.cyclone.solana.core.datamodel.entity.Seed
import kotlinx.coroutines.flow.Flow

@Dao
interface SeedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveSeeds(vararg seed: Seed)

    @Transaction
    @Query("select * from tbl_seeds limit 1")
    fun getSeed(): Flow<Seed?>

    @Transaction
    @Query("select * from tbl_seeds")
    fun getSeedAllSeeds(): Flow<List<Seed>>

    @Delete
    fun deleteSeeds(vararg seed: Seed)

    @Transaction
    @Query("delete from tbl_seeds")
    fun deleteAllSeeds()
}