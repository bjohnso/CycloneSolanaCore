package com.cyclone.solana.core.datamodel.dto.solanaRPC

import com.cyclone.solana.core.constants.RPC
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap

sealed class RPCResponse {
    data class Response(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        val result: Any?,
        val error: Error?
    )

    data class SuccessResponse(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        private var result: Any
    ) {
        val specificResult: Any get() = when(method) {
            RPC.RPCMethods.GET_BALANCE -> {
                val json = Gson().toJson(result as LinkedTreeMap<*, *>)
                val pojo = Gson().fromJson(json, Result::class.java)
                Result.LongResult.fromResult(pojo)
            }
            RPC.RPCMethods.GET_LATEST_BLOCKHASH -> {
                val json = Gson().toJson(result as LinkedTreeMap<*, *>)
                val pojo = Gson().fromJson(json, Result::class.java)
                Result.JsonResult.fromResult(pojo)
            }
            RPC.RPCMethods.SEND_TRANSACTION -> {
                Result.StringResult(
                    value = result as? String ?: ""
                )
            }
            RPC.RPCMethods.GET_TRANSACTION -> {
                val json = Gson().toJson(result as LinkedTreeMap<*, *>)
                Gson().fromJson(json, TransactionResult::class.java)
            }
            else -> result
        }
    }

    data class ErrorResponse(
        val jsonrpc: String,
        val id: Int,
        var method: String,
        private var result: Any?,
        val error: Error
    ) {
        val specificResult: Any? get() = when(method) {
            RPC.RPCMethods.GET_TRANSACTION -> {
                val json = Gson().toJson(result as LinkedTreeMap<*, *>)
                Gson().fromJson(json, TransactionResult::class.java)
            }
            else -> result
        }
    }
}
