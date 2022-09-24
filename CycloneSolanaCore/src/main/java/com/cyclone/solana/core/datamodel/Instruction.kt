package com.cyclone.solana.core.datamodel

import com.cyclone.solana.core.extensions.flatten
import com.cyclone.solana.core.usecase.CompactArrayEncoder

open class Instruction(
    open val programIdIndex: Int,
    open val accountIndices: List<Int>,
    open val data: ByteArray
) {
    fun serialise(): ByteArray {
        val programIdBytes = ByteArray(1).apply {
            this.fill(programIdIndex.toByte())
        }

        val accountIndicesBytes = accountIndices.map {
            CompactArrayEncoder.invoke(
                ByteArray(1).apply { this.fill(it.toByte()) }
            )
        }.flatten()

        val dataBytes = CompactArrayEncoder.invoke(
            data
        )

        return programIdBytes + accountIndicesBytes + dataBytes
    }
}
