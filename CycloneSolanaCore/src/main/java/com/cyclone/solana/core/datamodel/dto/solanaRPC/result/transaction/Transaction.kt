package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val message: Message? = null,
    val signatures: List<String>? = null,
)
