package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class TokenAmount(
    val amount: String? = null,
    val decimals: Int? = null,
    val uiAmount: Double? = null,
    val uiAmountString: String? = null,
)
