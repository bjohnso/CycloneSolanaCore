package com.cyclone.solana.core.datamodel.dto.solanaRPC

import androidx.annotation.Keep

@Keep
data class RPCRequest(
    val method: String,
    val params: List<Any>
) {
    val jsonrpc: String = "2.0"
    val id: Int = 1
}
