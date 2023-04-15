package com.cyclone.solana.core.http.dispatcher

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*

interface MockRequest {
    fun getSuccessResponse(): MockRequestHandler
    fun getErrorResponse(): MockRequestHandler
    fun getMalformedResponse(): MockRequestHandler
    suspend fun validateRequest(request: HttpRequestData): Boolean
}