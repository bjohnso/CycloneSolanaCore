package com.cyclone.solana.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.cyclone.solana.core.database.dao.KeyPairDao
import com.cyclone.solana.core.database.dao.SeedDao
import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.datamodel.entity.KeyPair
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteDatabaseHook
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        Seed::class,
        KeyPair::class
    ], version = 1
)
abstract class CycloneDatabase: RoomDatabase() {
    abstract fun keyPairDao(): KeyPairDao
    abstract fun seedDao(): SeedDao

    companion object {
        @Volatile private var instance: CycloneDatabase? = null
        private val LOCK = Any()

        operator fun invoke(
            context: Context,
            password: String
        ) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(
                context = context,
                password = password
            ).also {
                instance = it
            }
        }

        fun getInstance(): CycloneDatabase? {
            return instance
        }

        private fun buildDatabase(
            context: Context,
            password: String
        ): CycloneDatabase {
            val passphrase: ByteArray = SQLiteDatabase.getBytes(password.toCharArray())
            val factory = SupportFactory(passphrase, object: SQLiteDatabaseHook {
                override fun preKey(database: SQLiteDatabase?) {}

                override fun postKey(database: SQLiteDatabase?) {}
            }, false)

            return databaseBuilder(context, CycloneDatabase::class.java, "cyclone_core.db")
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}