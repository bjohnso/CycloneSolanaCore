package com.cyclone.solana.core.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cyclone.solana.core.database.CycloneDatabase
import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.extensions.hexToByteArray
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class SeedPersistenceUnitTest {
    private lateinit var database: CycloneDatabase

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CycloneDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun derived_seed_is_persisted() = runBlocking {
        val hexSeed = "b35cd271ce77c440d5aac7fd591403e3"

        val seed = Seed(
            hex = hexSeed,
            seed = hexSeed.hexToByteArray()
        )

        database.seedDao().saveSeeds(seed)

        var retrievedSeed: Seed? = null

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            retrievedSeed = database.seedDao().getSeed()
            latch.countDown()
        }

        latch.await()
        job.cancelAndJoin()

        assertEquals(hexSeed, retrievedSeed?.hex)
        assertArrayEquals(hexSeed.hexToByteArray(), retrievedSeed?.seed)
    }

    @Test
    fun persisted_seed_is_deleted() = runBlocking {
        val hexSeed = "b35cd271ce77c440d5aac7fd591403e3"

        val seed = Seed(
            hex = hexSeed,
            seed = hexSeed.hexToByteArray()
        )

        database.seedDao().saveSeeds(seed)

        var retrievedSeed: Seed? = null

        val saveLatch = CountDownLatch(1)
        val saveJob = async(Dispatchers.IO) {
            retrievedSeed = database.seedDao().getSeed()
            saveLatch.countDown()
        }

        saveLatch.await()
        saveJob.cancelAndJoin()

        assertEquals(hexSeed, retrievedSeed?.hex)
        assertArrayEquals(hexSeed.hexToByteArray(), retrievedSeed?.seed)

        database.seedDao().deleteSeeds(seed)

        val deleteLatch = CountDownLatch(1)
        val deleteJob = async(Dispatchers.IO) {
            retrievedSeed = database.seedDao().getSeed()
            deleteLatch.countDown()
        }

        deleteLatch.await()
        deleteJob.cancelAndJoin()

        assertNull(retrievedSeed)
    }
}