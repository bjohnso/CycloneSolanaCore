package com.cyclone.solana.core.datamodel.dto

import com.cyclone.solana.core.usecase.CompactArrayEncoder
import com.cyclone.solana.core.usecase.Ed25119Signer
import org.bouncycastle.crypto.AsymmetricCipherKeyPair

data class Transaction(
    var noSignatures: Int = 0,
    var signatures: ByteArray = ByteArray(0),
    val message: Message
) {
    fun serialise(): ByteArray {
        val signaturesBytes = serialiseSignatures()
        val messageBytes = serialiseMessage()

        return signaturesBytes + messageBytes
    }

    fun sign(keyPairs: List<AsymmetricCipherKeyPair>) {
        noSignatures = 0
        signatures = ByteArray(0)

        for (keyPair in keyPairs) {
            signatures += Ed25119Signer.invoke(
                serialiseMessage().copyOf(),
                keyPair
            )

            noSignatures++
        }
    }

    private fun serialiseSignatures(): ByteArray {
        return if (noSignatures > 0) {
            val compactU16 = CompactArrayEncoder.invoke(
                noSignatures
            )

            compactU16 + signatures
        } else ByteArray(0)
    }

    private fun serialiseMessage(): ByteArray {
        return message.serialise()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        if (!signatures.contentEquals(other.signatures)) return false
        if (message != other.message) return false

        return true
    }

    override fun hashCode(): Int {
        var result = signatures.contentHashCode()
        result = 31 * result + message.hashCode()
        return result
    }
}
