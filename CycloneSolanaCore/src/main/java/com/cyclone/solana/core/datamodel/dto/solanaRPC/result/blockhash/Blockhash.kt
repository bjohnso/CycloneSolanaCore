package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.blockhash

import kotlinx.serialization.Serializable

@Serializable
data class Blockhash(
    val blockhash: String,
    val lastValidBlockHeight: Long
)
