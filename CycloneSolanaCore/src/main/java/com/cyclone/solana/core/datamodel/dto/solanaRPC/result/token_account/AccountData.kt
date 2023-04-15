package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val parsed: Parsed? = null,
    val program: String? = null,
    val space: Long? = null,
)
