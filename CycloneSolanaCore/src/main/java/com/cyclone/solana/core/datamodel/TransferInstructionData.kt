package com.cyclone.solana.core.datamodel

import com.cyclone.solana.core.constants.SystemProgram
import com.cyclone.solana.core.extensions.toLEByteArray
import com.cyclone.solana.core.usecase.CompactArrayEncoder

data class TransferInstructionData(val lamports: Int): InstructionData {
    override fun serialise(): ByteArray {
        val functionIndexBytes = SystemProgram.TRANSFER
            .toLEByteArray(4) /* u32 */

        val lamportsBytes = lamports
            .toLEByteArray(8) /* u64 */

        val size = functionIndexBytes.size + lamportsBytes.size

        val compactU16 = CompactArrayEncoder.invoke(
            size
        )

        return compactU16 +
                functionIndexBytes +
                lamportsBytes
    }
}
