package com.cyclone.solana.core.datamodel.dto

interface InstructionData {
    fun serialise(): ByteArray
}