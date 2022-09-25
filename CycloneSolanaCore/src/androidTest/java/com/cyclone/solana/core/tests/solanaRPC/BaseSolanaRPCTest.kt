package com.cyclone.solana.core.tests.solanaRPC

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.rule.GrantPermissionRule
import com.cyclone.solana.core.http.client.HttpClient
import com.cyclone.solana.core.network.api.interfaces.SolanaRPCApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseSolanaRPCTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var solanaRPCApi: SolanaRPCApi

    protected val mockWebServer = MockWebServer()

    @Before
    fun setUpMockServer() {
        mockWebServer.start(8080)

        solanaRPCApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockWebServer.url("/").toString())
            .client(HttpClient.getOkHttpClient())
            .build()
            .create(SolanaRPCApi::class.java)
    }

}