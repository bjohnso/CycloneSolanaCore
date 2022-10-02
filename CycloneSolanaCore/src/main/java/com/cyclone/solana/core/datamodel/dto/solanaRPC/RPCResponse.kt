package com.cyclone.solana.core.datamodel.dto.solanaRPC

import com.cyclone.solana.core.constants.RPC

sealed class RPCResponse {
    data class Response(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        val result: Result? = null,
        val error: Error? = null
    )

    data class SuccessResponse(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        private var result: Result
    ) {
        val specificResult get() = when(method) {
            RPC.RPCMethods.GET_BALANCE -> {
                Result.LongResult.fromResult(result)
            }
            RPC.RPCMethods.GET_LATEST_BLOCKHASH -> {
                Result.JsonResult.fromResult(result)
            }
            RPC.RPCMethods.SEND_TRANSACTION -> {
                result
            }
            else -> result
        }
    }

    data class ErrorResponse(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        val error: Error
    )
}
