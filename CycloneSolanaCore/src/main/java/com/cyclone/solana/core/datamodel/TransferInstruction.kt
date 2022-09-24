package com.cyclone.solana.core.datamodel

data class TransferInstruction(
    override val programIdIndex: Int,
    override val accountIndices: List<Int>,
    override val data: InstructionData
): Instruction(
    programIdIndex = programIdIndex,
    accountIndices = accountIndices,
    data = data
)
