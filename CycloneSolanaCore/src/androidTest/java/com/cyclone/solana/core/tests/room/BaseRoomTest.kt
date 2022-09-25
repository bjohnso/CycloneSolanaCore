package com.cyclone.solana.core.tests.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cyclone.solana.core.database.CycloneDatabase
import org.junit.After
import org.junit.Before

open class BaseRoomTest {
    lateinit var database: CycloneDatabase

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
}