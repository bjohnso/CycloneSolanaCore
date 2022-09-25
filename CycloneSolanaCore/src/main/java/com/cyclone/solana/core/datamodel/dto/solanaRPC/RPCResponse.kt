package com.cyclone.solana.core.datamodel.entity

import com.cyclone.solana.core.datamodel.dto.solanaRPC.Result

data class RPCResponse(
    val jsonrpc: String,
    val id: Int,
    val result: Result
)
