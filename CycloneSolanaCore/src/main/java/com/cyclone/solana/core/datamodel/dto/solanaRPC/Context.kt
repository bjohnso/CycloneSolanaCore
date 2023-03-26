package com.cyclone.solana.core.datamodel.dto.solanaRPC

import androidx.annotation.Keep
@Keep
data class Context(
    val apiVersion: String,
    val slot: Int
)
