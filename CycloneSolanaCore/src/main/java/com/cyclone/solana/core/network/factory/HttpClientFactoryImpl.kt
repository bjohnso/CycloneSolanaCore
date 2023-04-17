package com.cyclone.solana.core.network.factory

import android.util.Log
import com.cyclone.solana.core.BuildConfig
import com.cyclone.solana.core.network.constants.HttpClientHeaders
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

class HttpClientFactoryImpl: HttpClientFactory {
    override fun createOkHttpClient(
        host: String?,
        connectTimeoutMillis: Long?,
        readTimeoutMillis: Long?,
    ) = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
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
                    if (BuildConfig.DEBUG)
                        Log.v("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                if (BuildConfig.DEBUG)
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