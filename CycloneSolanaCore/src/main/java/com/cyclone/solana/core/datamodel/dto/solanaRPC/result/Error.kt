package com.cyclone.solana.core.datamodel.dto.solanaRPC.result

import androidx.annotation.Keep

@Keep
@kotlinx.serialization.Serializable
data class Error(
    val code: Int? = null,
    val message: String? = null,
    val data: String? = null
)
