package com.cyclone.solana.core.datamodel

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
        // TODO: SERIALISE SIGNATURES
        return ByteArray(0)
    }

    private fun serialiseMessage(): ByteArray {
        return message.serialise()
    }
}
