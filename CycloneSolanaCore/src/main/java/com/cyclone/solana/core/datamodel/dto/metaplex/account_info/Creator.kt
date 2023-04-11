package com.cyclone.solana.core.datamodel.dto.metaplex.account_info

import kotlinx.serialization.Serializable

@Serializable
data class Creator constructor(
    val address: String,
    val verified: Boolean,
    val share: Int
)