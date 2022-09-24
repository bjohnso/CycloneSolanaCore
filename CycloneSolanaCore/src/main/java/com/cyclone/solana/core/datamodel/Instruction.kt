package com.cyclone.solana.core.datamodel

open class Instruction(
    open val programIdIndex: Int,
    open val accountAddressIndices: List<Int>,
    open val data: ByteArray
)
