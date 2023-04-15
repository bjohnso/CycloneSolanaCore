package com.cyclone.solana.core.http.client

import android.util.Log
import com.cyclone.solana.core.network.constants.HttpClientHeaders
import com.cyclone.solana.core.network.factory.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MockHttpClientFactoryImpl(
    private val requestHandler: MockRequestHandler
): HttpClientFactory {
    override fun createOkHttpClient(
        host: String?,
        connectTimeoutMillis: Long?,
        readTimeoutMillis: Long?
    ) = HttpClient(MockEngine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
                prettyPrint = true
            })
        }

        engine {
            addHandler(requestHandler)
        }

        install(Logging) {
            logger = object: Logger {
                override fun log(message: String) {
                    Log.v("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.v("Ktor", "Response: ${response.status.value}")
            }
        }

        install(DefaultRequest) {
            host?.let {
                url(host)
            }
            header(HttpClientHeaders.CONTENT_TYPE, ContentType.Application.Json)
        }
    }
}