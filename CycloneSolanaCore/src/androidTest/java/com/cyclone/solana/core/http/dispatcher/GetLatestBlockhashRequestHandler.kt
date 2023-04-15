package com.cyclone.solana.core.http.dispatcher

import com.cyclone.solana.core.constants.RPC
import com.cyclone.solana.core.datamodel.dto.solanaRPC.request.RPCRequest
import com.cyclone.solana.core.file.FileReader
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

object GetLatestBlockhashRequestHandler: MockRequest {
    override fun getSuccessResponse(): MockRequestHandler {
        return { request ->
            when (validateRequest(request)) {
                true -> respond(
                    content = FileReader.instance.readJsonFile(
                        FileReader.FileResource.getLatestBlockHash
                    ),
                    headers = headersOf("Content-Type" to listOf("application/json"))
                )
                else -> error("Request validation failed")
            }
        }
    }

    override fun getErrorResponse(): MockRequestHandler {
        return { request ->
            when (validateRequest(request)) {
                true -> respond(
                    content = FileReader.instance.readJsonFile(
                        FileReader.FileResource.getLatestBlockHashError
                    ),
                    headers = headersOf("Content-Type" to listOf("application/json"))
                )
                else -> error("Request validation failed")
            }
        }
    }

    override fun getMalformedResponse(): MockRequestHandler {
        TODO("Not yet implemented")
    }

    override suspend fun validateRequest(request: HttpRequestData): Boolean {
        val requestJson = request.body.toByteArray().toString(Charsets.UTF_8)

        val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
            allowStructuredMapKeys = true
            prettyPrint = true
        }

        val requestModel = json.decodeFromString(
            RPCRequest.RequestLatestBlockhash.serializer(),
            requestJson
        )

        return requestModel.id == RPC.RPCMethodIds.GET_LATEST_BLOCKHASH &&
                requestModel.method == RPC.RPCMethods.GET_LATEST_BLOCKHASH
    }
}