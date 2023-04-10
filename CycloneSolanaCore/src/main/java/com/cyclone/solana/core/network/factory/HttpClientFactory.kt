package com.cyclone.solana.core.network.factory

import android.util.Log
import com.cyclone.solana.core.network.constants.HttpClientHeaders
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.concurrent.TimeUnit

class HttpClientFactory {
    fun createOkHttpClient(
        host: String,
        connectTimeoutMillis: Long? = null,
        readTimeoutMillis: Long? = null
    ) = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
                prettyPrint = true
            })

            engine {
                config {
                    connectTimeoutMillis?.let {
                        connectTimeout(it, TimeUnit.MILLISECONDS)
                    }
                    readTimeoutMillis?.let {
                        readTimeout(it, TimeUnit.MILLISECONDS)
                    }
                }
            }
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
            url(host)
            header(HttpClientHeaders.CONTENT_TYPE, ContentType.Application.Json)
        }
    }
}