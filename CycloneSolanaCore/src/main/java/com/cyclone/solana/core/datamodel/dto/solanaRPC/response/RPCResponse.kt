package com.cyclone.solana.core.datamodel.dto.solanaRPC.response

import androidx.annotation.Keep
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Error
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.datamodel.serializer.solanaRPC.RPCResponseSerializer
import kotlinx.serialization.SerialName

@Keep
sealed class RPCResponse {
    @kotlinx.serialization.Serializable(with = RPCResponseSerializer::class)
    @SerialName("RPCResponse.Response")
    open class Response(
        open val jsonrpc: String = "",
        open val id: Int = 0,
        open var method: String = "",
        open val result: Result? = null,
        open val error: Error? = null
    )

    data class SuccessResponse(
        override val jsonrpc: String = "",
        override val id: Int = 0,
        override var method: String = "",
        override var result: Result = Result()
    ): Response(jsonrpc, id, method, result, null)

    data class ErrorResponse(
        override val jsonrpc: String = "",
        override val id: Int = 0,
        override var method: String = "",
        override val error: Error? = null
    ): Response(jsonrpc, id, method, null, error)
}
