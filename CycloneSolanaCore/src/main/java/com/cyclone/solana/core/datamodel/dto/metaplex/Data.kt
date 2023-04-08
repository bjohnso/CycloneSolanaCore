package com.cyclone.solana.core.datamodel.dto.metaplex

import kotlinx.serialization.Serializable

@Serializable
data class Data constructor(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: Int,
    val creators: List<Creator>
)
