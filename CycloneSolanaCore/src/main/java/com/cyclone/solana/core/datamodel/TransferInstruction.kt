package com.cyclone.solana.core.datamodel

data class TransferInstruction(
    override val programIdIndex: Int,
    override val accountIndices: List<Int>,
    override val data: ByteArray
): Instruction(
    programIdIndex = programIdIndex,
    accountIndices = accountIndices,
    data = data
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransferInstruction

        if (programIdIndex != other.programIdIndex) return false
        if (accountIndices != other.accountIndices) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = programIdIndex
        result = 31 * result + accountIndices.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
