package com.cyclone.solana.core.datamodel.dto.metaplex.meta_data

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val files: List<File>? = null,
    val category: String? = null
)
