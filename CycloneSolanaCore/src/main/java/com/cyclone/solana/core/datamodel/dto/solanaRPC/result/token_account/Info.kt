package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val tokenAmount: TokenAmount? = null,
    val delegate: String? = null,
    val delegatedAmount: TokenAmount? = null,
    val state: String? = null,
    val isNative: Boolean? = null,
    val mint: String? = null,
    val owner: String? = null,
)
