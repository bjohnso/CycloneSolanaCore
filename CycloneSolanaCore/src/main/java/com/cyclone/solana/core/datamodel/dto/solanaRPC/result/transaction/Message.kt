package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val accountKeys: List<String>? = null,
    val header: Header? = null,
    val instructions: List<Instruction>? = null,
    val recentBlockhash: String? = null,
)
