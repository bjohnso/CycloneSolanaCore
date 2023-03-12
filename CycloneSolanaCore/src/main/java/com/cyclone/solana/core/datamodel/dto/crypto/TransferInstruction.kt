package com.cyclone.solana.core.datamodel.dto.crypto

import androidx.annotation.Keep
import com.cyclone.solana.core.datamodel.interfaces.crypto.InstructionData

@Keep
data class TransferInstruction(
    override val programIdIndex: Int,
    override val accountIndices: List<Int>,
    override val data: InstructionData
): Instruction(
    programIdIndex = programIdIndex,
    accountIndices = accountIndices,
    data = data
)
