package com.cyclone.solana.core.datamodel.dto.solanaRPC

import com.google.gson.JsonObject

data class Result(
    val context: Context,
    val value: JsonObject,
)
