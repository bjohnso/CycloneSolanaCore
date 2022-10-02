package com.cyclone.solana.core.tests.solanaRPC

import com.cyclone.solana.core.http.client.HttpClient
import com.cyclone.solana.core.network.api.interfaces.SolanaRPCApi
import com.cyclone.solana.core.repository.implementation.SolanaRPCRepositoryImpl
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit


open class BaseSolanaRPCTest {
    private lateinit var solanaRPCApi: SolanaRPCApi

    protected lateinit var solanaRPCRepository: SolanaRPCRepository
    protected lateinit var mockWebServer: MockWebServer

    @Before
    fun setUpMockServer() {
        val rootCertificate: HeldCertificate = HeldCertificate.Builder()
            .certificateAuthority(1)
            .build()

        val intermediateCertificate: HeldCertificate = HeldCertificate.Builder()
            .certificateAuthority(0)
            .signedBy(rootCertificate)
            .build()

        val localhost: String = InetAddress.getByName("localhost").canonicalHostName
        val serverCertificate: HeldCertificate = HeldCertificate.Builder()
            .addSubjectAlternativeName(localhost)
            .signedBy(intermediateCertificate)
            .build()

        val serverHandshakeCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .heldCertificate(serverCertificate, intermediateCertificate.certificate)
            .build()

        val clientCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(rootCertificate.certificate)
            .build()

        mockWebServer = MockWebServer()
        mockWebServer.useHttps(
            serverHandshakeCertificates.sslSocketFactory(),
            false
        )
        mockWebServer.start(8080)

        solanaRPCApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockWebServer.url("/").toString())
            .client(
                HttpClient.getOkHttpClient(
                    clientCertificates.sslSocketFactory(),
                    clientCertificates.trustManager
                )
            )
            .build()
            .create(SolanaRPCApi::class.java)

        solanaRPCRepository = SolanaRPCRepositoryImpl(solanaRPCApi)
    }

    @After
    fun tearDownMockWebServer() {
        mockWebServer.shutdown()
    }
}