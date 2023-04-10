package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@Serializable
data class TransactionMeta(
    val computeUnitsConsumed: Int? = null,
    val err: JsonElement? = null,
    val fee: Int? = null,
    val innerInstructions: JsonArray? = null,
    val loadedAddresses: LoadedAddresses? = null,
    val logMessages: List<String>? = null,
    val postBalances: List<Long>? = null,
    val postTokenBalances: JsonArray? = null,
    val preBalances: List<Long>? = null,
    val preTokenBalances: JsonArray? = null,
    val rewards: JsonArray? = null,
    val status: Status? = null,
)
