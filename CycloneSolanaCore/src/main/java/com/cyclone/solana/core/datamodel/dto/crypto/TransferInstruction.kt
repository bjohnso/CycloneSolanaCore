package com.cyclone.solana.core.datamodel.dto.crypto

import com.cyclone.solana.core.datamodel.interfaces.crypto.InstructionData

data class TransferInstruction(
    override val programIdIndex: Int,
    override val accountIndices: List<Int>,
    override val data: InstructionData
): Instruction(
    programIdIndex = programIdIndex,
    accountIndices = accountIndices,
    data = data
)
