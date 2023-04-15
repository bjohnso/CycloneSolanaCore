package com.cyclone.solana.core.datamodel.dto.solanaRPC.result

import androidx.annotation.Keep
import kotlinx.serialization.json.JsonElement

@Keep
@kotlinx.serialization.Serializable
data class Error(
    val code: Int? = null,
    val message: String? = null,
    val data: JsonElement? = null
)
