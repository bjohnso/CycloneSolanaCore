package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class TokenAccount(
    val account: Account? = null,
    val pubkey: String? = null
)