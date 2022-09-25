package com.cyclone.solana.core.datamodel.dto

data class Header(
    val noSigs: Int,
    val noSignedReadOnlyAccounts: Int,
    val noUnsignedReadOnlyAccounts: Int
) {
    fun serialise(): ByteArray {
        return ByteArray(3).apply {
            this[0] = noSigs.toByte()
            this[1] = noSignedReadOnlyAccounts.toByte()
            this[2] = noUnsignedReadOnlyAccounts.toByte()
        }
    }
}
