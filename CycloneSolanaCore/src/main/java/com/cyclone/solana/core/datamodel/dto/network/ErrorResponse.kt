package com.cyclone.solana.core.datamodel.dto.network

import androidx.annotation.Keep

@Keep
data class ErrorResponse(
    val code: Int,
    val message: String
)
