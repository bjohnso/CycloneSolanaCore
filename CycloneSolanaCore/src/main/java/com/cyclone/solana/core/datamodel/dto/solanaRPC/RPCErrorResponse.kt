package com.cyclone.solana.core.datamodel.dto.solanaRPC

data class RPCErrorResponse(
    val jsonrpc: String,
    val id: Int,
    val method: String,
    val error: Error
)