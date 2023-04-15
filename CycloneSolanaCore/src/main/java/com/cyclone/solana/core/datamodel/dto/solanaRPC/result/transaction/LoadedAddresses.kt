package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class LoadedAddresses(
    val readonly: JsonArray? = null,
    val writable: JsonArray? = null
)
