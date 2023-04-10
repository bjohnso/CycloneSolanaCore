package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class Parsed(
    val accountType: String? = null,
    val info: Info? = null,
    val type: String? = null,
)