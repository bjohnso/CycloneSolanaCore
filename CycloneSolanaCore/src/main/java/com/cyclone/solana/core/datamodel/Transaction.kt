package com.cyclone.solana.core.datamodel

import com.cyclone.solana.core.extensions.flatten
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.CompactArrayEncoder

data class Transaction(
    val signatures: List<String>,
    val message: Message
) {
    fun serialise(): ByteArray {
        val signaturesBytes = serialiseSignatures()
        val messageBytes = serialiseMessage()

        return signaturesBytes + messageBytes
    }

    private fun serialiseSignatures(): ByteArray {
        return signatures.map {
            CompactArrayEncoder.invoke(
                Base58Decoder.invoke(it)
            )
        }.flatten()
    }

    private fun serialiseMessage(): ByteArray {
        return message.serialise()
    }
}
