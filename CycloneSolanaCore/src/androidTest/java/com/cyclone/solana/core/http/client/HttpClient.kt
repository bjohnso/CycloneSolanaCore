package com.cyclone.solana.core.http.client

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object HttpClient {
    private const val TIMEOUT_CONNECT = 20
    private const val TIMEOUT_READ = 40

    fun getOkHttpClient(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
            .build()
    }
}