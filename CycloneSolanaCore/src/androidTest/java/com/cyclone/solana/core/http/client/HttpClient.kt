package com.cyclone.solana.core.http.client

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HttpClient {
    const val TIMEOUT_CONNECT = 20
    const val TIMEOUT_READ = 40

    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
            .build()
    }
}