package com.cyclone.solana.core.datamodel.interfaces.crypto

interface InstructionData {
    fun serialise(): ByteArray
}