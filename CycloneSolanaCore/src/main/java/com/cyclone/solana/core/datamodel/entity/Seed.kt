package com.cyclone.solana.core.datamodel.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_seeds")
data class Seed(
    @PrimaryKey
    @ColumnInfo(
        name = "hex",
        typeAffinity = ColumnInfo.TEXT
    )
    val hex: String,

    @ColumnInfo(
        name = "seed",
        typeAffinity = ColumnInfo.BLOB
    )
    val seed: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Seed

        if (hex != other.hex) return false
        if (!seed.contentEquals(other.seed)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hex.hashCode()
        result = 31 * result + seed.contentHashCode()
        return result
    }
}
