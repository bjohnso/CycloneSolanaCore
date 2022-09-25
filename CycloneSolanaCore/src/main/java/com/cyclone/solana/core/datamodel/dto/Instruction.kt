package com.cyclone.solana.core.datamodel.dto

import com.cyclone.solana.core.extensions.toByteArray
import com.cyclone.solana.core.usecase.CompactArrayEncoder

open class Instruction(
    open val programIdIndex: Int,
    open val accountIndices: List<Int>,
    open val data: InstructionData
) {
    fun serialise(): ByteArray {
        val programIdBytes = ByteArray(1).apply {
            this.fill(programIdIndex.toByte())
        }

        val accountIndicesCompactU16 = CompactArrayEncoder.invoke(
            accountIndices.size
        )

        val accountIndicesBytes = accountIndices
            .toIntArray()
            .toByteArray()

        val dataBytes = data.serialise()

        return programIdBytes +
                accountIndicesCompactU16 +
                accountIndicesBytes +
                dataBytes
    }
}
