package com.cyclone.solana.core.datamodel.dto

import com.cyclone.solana.core.extensions.flatten
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.CompactArrayEncoder

data class Message(
    val header: Header,
    val accountAddresses: List<String>,
    val blockhash: String,
    val instructions: List<Instruction>
) {
    fun serialise(): ByteArray {
        val headerBytes = serialiseHeader()
        val accountAddressesBytes = serialiseAccountAddresses()
        val blockhashBytes = serialiseBlockHash()
        val instructionsBytes = serialiseInstructions()

        return headerBytes + accountAddressesBytes + blockhashBytes + instructionsBytes
    }

    private fun serialiseHeader(): ByteArray {
        return header.serialise()
    }

    private fun serialiseAccountAddresses(): ByteArray {
        val compactU16 = CompactArrayEncoder.invoke(
            accountAddresses.size
        )

        val items = accountAddresses
            .map { Base58Decoder.invoke(it) }
            .flatten()

        return compactU16 + items
    }

    private fun serialiseBlockHash(): ByteArray {
        return Base58Decoder.invoke(blockhash)
    }

    private fun serialiseInstructions(): ByteArray {
        val compactU16 = CompactArrayEncoder.invoke(
            instructions.size
        )

        val items = instructions.map {
            it.serialise()
        }.flatten()

        return compactU16 + items
    }
}
