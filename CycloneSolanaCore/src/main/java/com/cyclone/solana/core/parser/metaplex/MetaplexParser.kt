package com.cyclone.solana.core.parser.metaplex

import com.cyclone.solana.core.datamodel.dto.metaplex.Creator
import com.cyclone.solana.core.datamodel.dto.metaplex.Data
import com.cyclone.solana.core.datamodel.dto.metaplex.MetaData
import com.cyclone.solana.core.extensions.toBase58
import java.nio.ByteBuffer
import java.nio.ByteOrder

object MetaplexParser {
    fun unpackMetadataAccount(data: ByteArray): MetaData {
        require(data[0].toInt() == 4)
        val buffer = ByteBuffer.wrap(data).apply { position(1) }

        fun readPublicKey(): ByteArray {
            val bytes = ByteArray(32)
            buffer.get(bytes)
            return bytes
        }

        val sourceAccount = readPublicKey()
        val mintAccount = readPublicKey()

        fun readString(): String {
            val length = buffer.order(ByteOrder.LITTLE_ENDIAN).int
            val bytes = ByteArray(length)
            buffer.get(bytes)
            return bytes.decodeToString().trim('\u0000')
        }

        val name = readString()
        val symbol = readString()
        val uri = readString()

        val fee = buffer.short.toInt()
        val hasCreator = buffer.get().toInt() != 0
        val creators = mutableListOf<Creator>()

        if (hasCreator) {
            val creatorLen = buffer.int
            repeat(creatorLen) {
                val address = readPublicKey()
                val verified = buffer.get().toInt() != 0
                val share = buffer.get().toInt()
                creators.add(Creator(address.toBase58(), verified, share))
            }
        }

        val primarySaleHappened = buffer.get().toInt() != 0
        val isMutable = buffer.get().toInt() != 0

        return MetaData(
            updateAuthority = sourceAccount.toBase58(),
            mint = mintAccount.toBase58(),
            data = Data(
                name = name,
                symbol = symbol,
                uri = uri,
                sellerFeeBasisPoints = fee,
                creators = creators
            ),
            primarySaleHappened = primarySaleHappened,
            isMutable = isMutable
        )
    }
}