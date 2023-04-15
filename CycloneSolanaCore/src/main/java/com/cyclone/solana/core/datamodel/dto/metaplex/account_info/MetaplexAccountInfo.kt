package com.cyclone.solana.core.datamodel.dto.metaplex.account_info

import kotlinx.serialization.Serializable

@Serializable
data class MetaplexAccountInfo constructor(
    val updateAuthority: String,
    val mint: String,
    val data: Data,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean
)

