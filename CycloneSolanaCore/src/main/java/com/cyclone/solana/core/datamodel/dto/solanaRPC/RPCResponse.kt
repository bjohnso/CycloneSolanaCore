package com.cyclone.solana.core.datamodel.dto.solanaRPC

sealed class RPCResponse {
    data class Response(
        val jsonrpc: String,
        val id: Int,
        val method: String,
        val result: Result? = null,
        val error: Error? = null
    )

    data class SuccessResponse(
        val jsonrpc: String,
        val id: Int,
        val method: String,
        val result: Result
    )

    data class ErrorResponse(
        val jsonrpc: String,
        val id: Int,
        val method: String,
        val error: Error
    )
}
