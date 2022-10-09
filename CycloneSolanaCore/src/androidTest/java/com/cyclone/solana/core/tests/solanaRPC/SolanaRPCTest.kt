package com.cyclone.solana.core.tests.solanaRPC

import com.cyclone.solana.core.constants.Commitment
import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.Result
import com.cyclone.solana.core.datamodel.dto.solanaRPC.TransactionResult
import com.cyclone.solana.core.http.client.HttpClient
import com.cyclone.solana.core.http.dispatcher.GetBalanceDispatcher
import com.cyclone.solana.core.http.dispatcher.GetTransactionDispatcher
import com.cyclone.solana.core.http.dispatcher.LatestBlockhashDispatcher
import com.cyclone.solana.core.http.dispatcher.SendTransactionDispatcher
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.interfaces.SolanaRPCApi
import com.cyclone.solana.core.repository.implementation.SolanaRPCRepositoryImpl
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SolTransferTransaction
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress

class SolanaRPCTest {
    private lateinit var solanaRPCApi: SolanaRPCApi
    private lateinit var solanaRPCRepository: SolanaRPCRepository
    private lateinit var mockWebServer: MockWebServer

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

    @Test
    fun get_blockhash_is_correct() {
        mockWebServer.dispatcher = LatestBlockhashDispatcher.getSuccessResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getLatestBlockHash().collect {
                emissions.add(it)
            }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        assertTrue(success.result.specificResult is Result.JsonResult)

        val result = success.result.specificResult as Result.JsonResult

        val responseBlockhash = result.value
            .getAsJsonPrimitive("blockhash")
            ?.asString

        assertEquals(
            "GpTSZjernhsXHeKG7K5zuBtojwkGnz9zRgcWH45zHkHb",
            responseBlockhash
        )
    }

    @Test
    fun get_blockhash_is_error() {
        mockWebServer.dispatcher = LatestBlockhashDispatcher.getErrorResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getLatestBlockHash().collect {
                emissions.add(it)
            }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Error)

        val error = emissions[1] as NetworkResource.Error
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32600,
            responseErrorCode
        )
    }

    @Test
    fun get_balance_is_correct() {
        mockWebServer.dispatcher = GetBalanceDispatcher.getSuccessResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getBalance(address = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
                .collect {
                    emissions.add(it)
                }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        assertTrue(success.result.specificResult is Result.LongResult)

        val result = success.result.specificResult as Result.LongResult
        val responseBalance = result.value

        assertEquals(
            11653589636,
            responseBalance
        )
    }

    @Test
    fun get_balance_is_error() {
        mockWebServer.dispatcher = GetBalanceDispatcher.getErrorResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getBalance(address = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
                .collect {
                    emissions.add(it)
                }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Error)

        val error = emissions[1] as NetworkResource.Error
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32600,
            responseErrorCode
        )
    }

    @Test
    fun send_lamports_did_succeed() {
        mockWebServer.dispatcher = SendTransactionDispatcher.getSuccessResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        val publicKeyParameters = Ed25519PublicKeyParameters(
            Base58Decoder.invoke("DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
        )

        val privateKeyParameters = Ed25519PrivateKeyParameters(
            Base58Decoder.invoke("7oLVLF7Pwxdwc6BUqLGdTgg6y5hMcgW3gkwwRQsiRrRT")
        )

        val keyPair = AsymmetricCipherKeyPair(
            publicKeyParameters,
            privateKeyParameters
        )

        val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
        val toAddress = "W5RJACTF8t4TC1LSBr4SXqkuZkFMM8yZFvRqqfM6tqM"
        val blockhash = "FBbwSyXd6KvK69g8ziWdPjJfegRgUuCDm2RbViKoD26Q"
        val lamports = Unit.Units.LAMPORTS_PER_SOL

        val transaction = SolTransferTransaction.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        ).apply {
            sign(listOf(keyPair))
        }.serialise()

        runBlocking {
            solanaRPCRepository
                .sendTransaction(Base58Encoder.invoke(transaction))
                .collect {
                    emissions.add(it)
                }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        assertTrue(success.result.specificResult is Result.StringResult)

        val result = success.result.specificResult as Result.StringResult
        val responseSignature = result.value

        assertEquals(
            "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
            responseSignature
        )
    }

    @Test
    fun send_lamports_did_error() {
        mockWebServer.dispatcher = SendTransactionDispatcher.getErrorResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        val publicKeyParameters = Ed25519PublicKeyParameters(
            Base58Decoder.invoke("DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
        )

        val privateKeyParameters = Ed25519PrivateKeyParameters(
            Base58Decoder.invoke("7oLVLF7Pwxdwc6BUqLGdTgg6y5hMcgW3gkwwRQsiRrRT")
        )

        val keyPair = AsymmetricCipherKeyPair(
            publicKeyParameters,
            privateKeyParameters
        )

        val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
        val toAddress = "W5RJACTF8t4TC1LSBr4SXqkuZkFMM8yZFvRqqfM6tqM"
        val blockhash = "FBbwSyXd6KvK69g8ziWdPjJfegRgUuCDm2RbViKoD26Q"
        val lamports = Unit.Units.LAMPORTS_PER_SOL

        val transaction = SolTransferTransaction.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        ).apply {
            sign(listOf(keyPair))
        }.serialise()

        runBlocking {
            solanaRPCRepository
                .sendTransaction(Base58Encoder.invoke(transaction))
                .collect {
                    emissions.add(it)
                }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Error)

        val error = emissions[1] as NetworkResource.Error
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32002,
            responseErrorCode
        )
    }

    @Test
    fun get_transaction_did_succeed() {
        mockWebServer.dispatcher = GetTransactionDispatcher.getSuccessResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getTransaction(
                    transactionSignature = "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
                    commitment = Commitment.Commitment.CONFIRMED
                )
                .collect {
                    emissions.add(it)
                }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        assertTrue(success.result.specificResult is TransactionResult)

        val result = success.result.specificResult as TransactionResult
        val responseBlockTime = result.blockTime

        assertEquals(
            1664719638,
            responseBlockTime
        )
    }

    @Test
    fun get_transaction_did_not_succeed() {
        mockWebServer.dispatcher = GetTransactionDispatcher.getErrorResponse()

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getTransaction(
                transactionSignature = "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
                commitment = Commitment.Commitment.CONFIRMED
            ).collect {
                emissions.add(it)
            }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Error)

        val error = emissions[1] as NetworkResource.Error

        assertTrue(error.error?.specificResult is TransactionResult)

        val result = error.error?.specificResult as TransactionResult
        val responseBlockTime = result.blockTime

        assertEquals(
            1664719638,
            responseBlockTime
        )
    }
}