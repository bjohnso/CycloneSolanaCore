package com.cyclone.solana.core.datamodel.dto.metaplex.meta_data

import kotlinx.serialization.Serializable

@Serializable
data class MetaplexMetaData(
    val name: String? = null,
    val symbol: String? = null,
    val description: String? = null,
    val image: String? = null,
    val properties: Properties? = null
)
