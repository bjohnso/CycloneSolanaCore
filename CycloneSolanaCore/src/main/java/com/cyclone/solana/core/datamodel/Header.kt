package com.cyclone.solana.core.datamodel

data class Header(
    val noSigs: Int,
    val noSignedReadOnlyAccounts: Int,
    val noUnsignedReadOnlyAccounts: Int
)
