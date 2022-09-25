package com.cyclone.solana.core.datamodel.entity

import androidx.room.*

@Entity(tableName = "tbl_keypairs")
data class KeyPair(
    @PrimaryKey
    @ColumnInfo(
        name = "public_key",
        typeAffinity = ColumnInfo.TEXT
    )
    val publicKey: String,

    @ColumnInfo(
        name = "private_key",
        typeAffinity = ColumnInfo.BLOB
    )
    val privateKey: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeyPair

        if (publicKey != other.publicKey) return false
        if (!privateKey.contentEquals(other.privateKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = publicKey.hashCode()
        result = 31 * result + privateKey.contentHashCode()
        return result
    }
}