package com.cyclone.solana.core.network.factory

import io.ktor.client.*

interface HttpClientFactory {
    fun createOkHttpClient(
        host: String? = null,
        connectTimeoutMillis: Long? = null,
        readTimeoutMillis: Long? = null,
    ): HttpClient
}