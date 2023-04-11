package com.cyclone.solana.core.datamodel.dto.metaplex.meta_data

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val uri: String? = null,
    val type: String? = null
)
