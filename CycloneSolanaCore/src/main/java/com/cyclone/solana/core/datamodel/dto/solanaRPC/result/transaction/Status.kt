package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Status(
    val Ok: JsonElement? = null
)
