package com.cyclone.solana.core.datamodel

data class TransferInstruction(
    override val programIdIndex: Int,
    override val accountAddressIndices: List<Int>,
    override val data: ByteArray
): Instruction(
    programIdIndex = programIdIndex,
    accountAddressIndices = accountAddressIndices,
    data = data
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransferInstruction

        if (programIdIndex != other.programIdIndex) return false
        if (accountAddressIndices != other.accountAddressIndices) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = programIdIndex
        result = 31 * result + accountAddressIndices.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
