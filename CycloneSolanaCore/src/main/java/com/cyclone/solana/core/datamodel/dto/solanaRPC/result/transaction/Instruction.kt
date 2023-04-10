package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Instruction(
    val accounts: List<Int>? = null,
    val data: String? = null,
    val programIdIndex: Int? = null,
)
