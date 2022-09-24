package com.cyclone.solana.core.datamodel

interface InstructionData {
    fun serialise(): ByteArray
}