package com.cyclone.solana.core.tests.room

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cyclone.solana.core.datamodel.entity.Seed
import com.cyclone.solana.core.extensions.hexToByteArray
import com.cyclone.solana.core.repository.implementation.SeedRepositoryImpl
import com.cyclone.solana.core.repository.interfaces.SeedRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class SeedPersistenceTest: BaseRoomTest() {
    lateinit var seedRepository: SeedRepository

    @Before
    fun setupRepository() {
        seedRepository = SeedRepositoryImpl(database)
    }

    @Test
    fun derived_seed_is_persisted() = runBlocking {
        val hexSeed = "b35cd271ce77c440d5aac7fd591403e3"

        val seed = Seed(
            hex = hexSeed,
            seed = hexSeed.hexToByteArray()
        )

        seedRepository.saveSeeds(seed)

        var retrievedSeed: Seed? = null

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            retrievedSeed = seedRepository.getSeed().first()
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

        seedRepository.saveSeeds(seed)

        var retrievedSeed: Seed? = null

        val saveLatch = CountDownLatch(1)
        val saveJob = async(Dispatchers.IO) {
            retrievedSeed = seedRepository.getSeed().first()
            saveLatch.countDown()
        }

        saveLatch.await()
        saveJob.cancelAndJoin()

        assertEquals(hexSeed, retrievedSeed?.hex)
        assertArrayEquals(hexSeed.hexToByteArray(), retrievedSeed?.seed)

        seedRepository.deleteSeeds(seed)

        val deleteLatch = CountDownLatch(1)
        val deleteJob = async(Dispatchers.IO) {
            retrievedSeed = seedRepository.getSeed().first()
            deleteLatch.countDown()
        }

        deleteLatch.await()
        deleteJob.cancelAndJoin()

        assertNull(retrievedSeed)
    }
}