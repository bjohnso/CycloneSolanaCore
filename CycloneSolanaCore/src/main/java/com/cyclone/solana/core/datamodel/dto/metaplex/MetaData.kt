package com.cyclone.solana.core.datamodel.dto.metaplex

import kotlinx.serialization.Serializable

@Serializable
data class MetaData constructor(
    val updateAuthority: String,
    val mint: String,
    val data: Data,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean
)

