package com.cyclone.solana.core.datamodel.dto.solanaRPC.request

import androidx.annotation.Keep
import com.cyclone.solana.core.constants.Address
import com.cyclone.solana.core.constants.RPC
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Keep
sealed class RPCRequest {
    @Serializable
    data class RequestBalance(
        @Transient val address: String = "",
        val jsonrpc: String = "2.0",
    ) {
        val id: Int = RPC.RPCMethodIds.GET_BALANCE
        val method: String = RPC.RPCMethods.GET_BALANCE
        val params: List<String> = listOf(address)
    }

    @Serializable
    data class RequestLatestBlockhash(
        @Transient val commitment: String = "finalized",
        val jsonrpc: String = "2.0",
    ) {
        val id: Int = RPC.RPCMethodIds.GET_LATEST_BLOCKHASH
        val method: String = RPC.RPCMethods.GET_LATEST_BLOCKHASH
        val params = listOf(
            JsonObject(
                mapOf(
                    Pair("commitment", JsonPrimitive(commitment)),
                )
            )
        )
    }

    @Serializable
    data class RequestSendTransaction(
        @Transient val transactions: List<String> = listOf(),
        val jsonrpc: String = "2.0",
    ) {
        val id: Int = RPC.RPCMethodIds.SEND_TRANSACTION
        val method: String = RPC.RPCMethods.SEND_TRANSACTION
        val params = transactions
    }

    @Serializable
    data class RequestGetTransaction(
        @Transient val transactionSignature: String = "",
        @Transient val commitment: String = "finalized",
        val jsonrpc: String = "2.0",
    ) {
        val id: Int = RPC.RPCMethodIds.GET_TRANSACTION
        val method: String = RPC.RPCMethods.GET_TRANSACTION
        val params = listOf(
            JsonPrimitive(transactionSignature),
            JsonObject(
                mapOf(
                    Pair("commitment", JsonPrimitive(commitment)),
                )
            )
        )
    }

    @Serializable
    data class RequestGetTokenAccountsByOwner(
        @Transient val address: String = "",
        @Transient val mint: String = "",
        @Transient val commitment: String = "finalized",
        val jsonrpc: String = "2.0",
    ) {
        @Transient val encoding: String = "jsonParsed"
        @Transient val programId: String = Address.ProgramAddresses.TOKEN_PROGRAM

        val id: Int = RPC.RPCMethodIds.GET_TOKEN_ACCOUNTS_BY_OWNER
        val method: String = RPC.RPCMethods.GET_TOKEN_ACCOUNTS_BY_OWNER
        val params = listOf(
            JsonPrimitive(address),
            JsonObject(
                mapOf(
                    Pair("mint", JsonPrimitive(mint)),
                    Pair("programId", JsonPrimitive(programId)),
                ).filter {
                    it.value.content.isNotBlank() && (it.key != "programId" || mint.isBlank())
                }
            ),
            JsonObject(
                mapOf(
                    Pair("encoding", JsonPrimitive(encoding)),
                    Pair("commitment", JsonPrimitive(commitment)),
                )
            )
        )
    }

    @Serializable
    data class RequestGetAccountInfo(
        @Transient val address: String = "",
        @Transient val commitment: String = "finalized",
        val jsonrpc: String = "2.0",
    ) {
        @Transient val encoding: String = "base64"

        val id: Int = RPC.RPCMethodIds.GET_ACCOUNT_INFO
        val method: String = RPC.RPCMethods.GET_ACCOUNT_INFO
        val params = listOf(
            JsonPrimitive(address),
            JsonObject(
                mapOf(
                    Pair("encoding", JsonPrimitive(encoding)),
                    Pair("commitment", JsonPrimitive(commitment)),
                )
            )
        )
    }
}
