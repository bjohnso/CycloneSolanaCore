package com.cyclone.solana.core.http.dispatcher

import com.cyclone.solana.core.file.FileReader
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*

object GetNFTMetaDataRequestHandler: MockRequest {
    override fun getSuccessResponse(): MockRequestHandler {
        return { request ->
            when (validateRequest(request)) {
                true -> respond(
                    content = FileReader.instance.readJsonFile(
                        FileReader.FileResource.getNFTMetaData
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
                true -> respondBadRequest()
                else -> error("Request validation failed")
            }
        }
    }

    override fun getMalformedResponse(): MockRequestHandler {
        TODO("Not yet implemented")
    }

    override suspend fun validateRequest(request: HttpRequestData): Boolean = true
}