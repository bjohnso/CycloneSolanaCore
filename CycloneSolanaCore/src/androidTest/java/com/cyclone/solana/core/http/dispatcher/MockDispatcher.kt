package com.cyclone.solana.core.http.dispatcher

import okhttp3.mockwebserver.Dispatcher

interface MockDispatcher {
    fun getSuccessResponse(): Dispatcher
    fun getErrorResponse(): Dispatcher
    fun getMalformedResponse(): Dispatcher
}