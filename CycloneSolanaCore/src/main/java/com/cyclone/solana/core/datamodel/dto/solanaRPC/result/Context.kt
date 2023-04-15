package com.cyclone.solana.core.datamodel.dto.solanaRPC.result

import androidx.annotation.Keep

@Keep
@kotlinx.serialization.Serializable
data class Context(
    val apiVersion: String,
    val slot: Int
)
