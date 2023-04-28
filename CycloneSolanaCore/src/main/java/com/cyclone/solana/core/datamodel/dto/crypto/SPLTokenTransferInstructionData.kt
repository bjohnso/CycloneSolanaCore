package com.cyclone.solana.core.datamodel.dto.crypto

import androidx.annotation.Keep
import com.cyclone.solana.core.constants.TokenProgram
import com.cyclone.solana.core.datamodel.interfaces.crypto.InstructionData
import com.cyclone.solana.core.extensions.toBEByteArray
import com.cyclone.solana.core.extensions.toLEByteArray
import com.cyclone.solana.core.usecase.CompactArrayEncoder

@Keep
class SPLTokenTransferInstructionData(private val amount: Long): InstructionData {
    override fun serialise(): ByteArray {
        val functionIndexBytes = byteArrayOf(TokenProgram.Enums.TRANSFER.toByte())

        val amountBytes = amount
            .toLEByteArray(8) /* u64 */

        val size = functionIndexBytes.size + amountBytes.size

        val compactU16 = CompactArrayEncoder.invoke(
            size
        )

        return compactU16 +
                functionIndexBytes +
                amountBytes
    }
}