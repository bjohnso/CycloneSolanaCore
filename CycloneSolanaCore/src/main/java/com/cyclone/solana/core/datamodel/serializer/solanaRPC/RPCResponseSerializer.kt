package com.cyclone.solana.core.datamodel.serializer.solanaRPC

import com.cyclone.solana.core.constants.RPC
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Error
import com.cyclone.solana.core.datamodel.dto.solanaRPC.response.RPCResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer

object RPCResponseSerializer: KSerializer<RPCResponse.Response> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RPCResponse.Response") {
        element("jsonrpc", String.serializer().descriptor)
        element("id", Int.serializer().descriptor)
        element("method", String.serializer().descriptor.nullable, isOptional = true)
        element("result", JsonElement.serializer().descriptor.nullable, isOptional = true)
        element("error", Error.serializer().descriptor.nullable, isOptional = true)
    }

    override fun deserialize(decoder: Decoder): RPCResponse.Response {
        val jsonElement = decoder.decodeSerializableValue(json.serializersModule.serializer<JsonObject>())
        val jsonObject = jsonElement as? JsonObject ?: throw IllegalStateException("Invalid JSON for Result")
        val id = jsonObject["id"]?.jsonPrimitive?.int ?: -1

        return when (val result = jsonObject["result"]) {
            is JsonElement -> {
                val parsedResult = when (id) {
                    RPC.RPCMethodIds.GET_LATEST_BLOCKHASH -> json.decodeFromJsonElement(
                        Result.GetLatestBlockhashResult.serializer(),
                        result
                    )
                    RPC.RPCMethodIds.GET_BALANCE -> json.decodeFromJsonElement(
                        Result.GetBalanceResult.serializer(),
                        result
                    )
                    RPC.RPCMethodIds.SEND_TRANSACTION -> json.decodeFromJsonElement(
                        Result.SendTransactionResult.serializer(),
                        result
                    )
                    RPC.RPCMethodIds.GET_TRANSACTION -> json.decodeFromJsonElement(
                        Result.GetTransactionResult.serializer(),
                        result
                    )
                    RPC.RPCMethodIds.GET_TOKEN_ACCOUNTS_BY_OWNER -> json.decodeFromJsonElement(
                        Result.GetTokenAccountsByOwnerResult.serializer(),
                        result
                    )
                    RPC.RPCMethodIds.GET_ACCOUNT_INFO -> json.decodeFromJsonElement(
                        Result.GetAccountInfoResult.serializer(),
                        result
                    )
                    else -> throw IllegalStateException("Unknown RPC method id: $id")
                }

                RPCResponse.SuccessResponse(
                    jsonrpc = jsonObject["jsonrpc"]?.jsonPrimitive?.content ?: "",
                    id = jsonObject["id"]?.jsonPrimitive?.int ?: 0,
                    method = jsonObject["method"]?.jsonPrimitive?.content ?: "",
                    result = parsedResult
                )
            }
            else -> RPCResponse.ErrorResponse(
                jsonrpc = jsonObject["jsonrpc"]?.jsonPrimitive?.content ?: "",
                id = jsonObject["id"]?.jsonPrimitive?.int ?: -1,
                method = jsonObject["method"]?.jsonPrimitive?.content ?: "",
                error = jsonObject["error"]?.jsonObject?.let {
                    json.decodeFromJsonElement(Error.serializer(), it)
                }
            )
        }
    }

    override fun serialize(encoder: Encoder, value: RPCResponse.Response) {
        val jsonElement = buildJsonObject {
            put("jsonrpc", JsonPrimitive(value.jsonrpc))
            put("id", JsonPrimitive(value.id))
            put("method", JsonPrimitive(value.method))

            when (value) {
                is RPCResponse.SuccessResponse -> {
                    put(
                        "result",
                        when (value.id) {
                            RPC.RPCMethodIds.GET_LATEST_BLOCKHASH -> json.encodeToJsonElement(
                                Result.GetLatestBlockhashResult.serializer(),
                                value.result as Result.GetLatestBlockhashResult
                            )
                            RPC.RPCMethodIds.GET_BALANCE -> json.encodeToJsonElement(
                                Result.GetBalanceResult.serializer(),
                                value.result as Result.GetBalanceResult
                            )
                            RPC.RPCMethodIds.SEND_TRANSACTION -> json.encodeToJsonElement(
                                Result.SendTransactionResult.serializer(),
                                value.result as Result.SendTransactionResult
                            )
                            RPC.RPCMethodIds.GET_TRANSACTION -> json.encodeToJsonElement(
                                Result.GetTransactionResult.serializer(),
                                value.result as Result.GetTransactionResult
                            )
                            RPC.RPCMethodIds.GET_TOKEN_ACCOUNTS_BY_OWNER -> json.encodeToJsonElement(
                                Result.GetTokenAccountsByOwnerResult.serializer(),
                                value.result as Result.GetTokenAccountsByOwnerResult
                            )
                            RPC.RPCMethodIds.GET_ACCOUNT_INFO -> json.encodeToJsonElement(
                                Result.GetAccountInfoResult.serializer(),
                                value.result as Result.GetAccountInfoResult
                            )
                            else -> throw IllegalStateException("Unknown RPC method id: ${value.id}")
                        }
                    )
                }
                is RPCResponse.ErrorResponse -> {
                    // Serialize the error field
                }
            }
        }

        encoder.encodeSerializableValue(JsonObject.serializer(), jsonElement)
    }
}