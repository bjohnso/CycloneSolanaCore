package com.cyclone.solana.core.datamodel.dto.solanaRPC

import androidx.annotation.Keep

@Keep
data class Error(
    val code: Int,
    val message: String
)
