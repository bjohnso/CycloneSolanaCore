package com.cyclone.solana.core.datamodel.dto.solanaRPC

data class RPCResponse(
    val jsonrpc: String,
    val id: Int,
    val method: String,
    val result: Result,
)
