package com.cyclone.solana.core.tests.solanaRPC

import com.cyclone.solana.core.constants.Unit.Units.LAMPORTS_PER_SOL
import com.cyclone.solana.core.datamodel.dto.solanaRPC.response.RPCResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.http.client.MockHttpClientFactoryImpl
import com.cyclone.solana.core.http.dispatcher.*
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.SolanaRPCApi
import com.cyclone.solana.core.repository.implementation.SolanaRPCApiRepositoryImpl
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SolTransferTransaction
import kotlinx.coroutines.runBlocking
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.junit.Assert.*
import org.junit.Test

class SolanaRPCTest {
    private lateinit var solanaRPCApi: SolanaRPCApi
    private lateinit var solanaRPCRepository: SolanaRPCRepository

    @Test
    fun get_blockhash_did_succeed() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetLatestBlockhashRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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

        if (emissions[1] is NetworkResource.Error) {
            println("ERROR")
            println((emissions[1] as NetworkResource.Error).error?.error?.message)
        }

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        assertTrue(success.result.result is Result.GetLatestBlockhashResult)

        val result = success.result.result as Result.GetLatestBlockhashResult

        val responseBlockhash = result.getLatestBlockhashResult.blockhash

        assertEquals(
            "GpTSZjernhsXHeKG7K5zuBtojwkGnz9zRgcWH45zHkHb",
            responseBlockhash
        )
    }

    @Test
    fun get_blockhash_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetLatestBlockhashRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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
    fun get_balance_did_succeed() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetBalanceRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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

        assertTrue(success.result.result is Result.GetBalanceResult)

        val result = success.result.result as Result.GetBalanceResult
        val responseBalance = result.getBalanceResult

        assertEquals(
            11653589636,
            responseBalance
        )
    }

    @Test
    fun get_balance_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetBalanceRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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
    fun send_transaction_did_succeed() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                SendTransactionRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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
        val lamports = LAMPORTS_PER_SOL

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

        assertTrue(success.result.result is Result.SendTransactionResult)

        val result = success.result.result as Result.SendTransactionResult
        val responseSignature = result.sendTransactionResult

        assertEquals(
            "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
            responseSignature
        )
    }

    @Test
    fun send_transaction_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                SendTransactionRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

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
        val lamports = LAMPORTS_PER_SOL

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
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetTransactionRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getTransaction(
                    transactionSignature = "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
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

        assertTrue(success.result.result is Result.GetTransactionResult)

        val result = success.result.result as Result.GetTransactionResult
        val responseBlockTime = result.getTransactionResult.blockTime

        assertEquals(
            1664719638L,
            responseBlockTime
        )
    }

    @Test
    fun get_transaction_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetTransactionRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getTransaction(
                transactionSignature = "2cDhsuZKVJoKCtDAtKGDD473yzodmGs9pRBu4TpD7Sp18FuPj5Zk1NKz3Dfk5GuDcQenRwLwBYAMExFjebYs48K2",
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
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32602,
            responseErrorCode
        )
    }

    @Test
    fun get_token_accounts_by_owner_did_succeed() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetTokenAccountsByOwnerRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getTokenAccountsByOwner(
                    address = "AQgMCXr8gcbjKmiB525YzcqQdVEZtAYVeBDWyDEBg9Z3"
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

        assertTrue(success.result.result is Result.GetTokenAccountsByOwnerResult)

        val result = success.result.result as Result.GetTokenAccountsByOwnerResult
        val tokenAccounts = result.getTokenAccountsByOwnerResult

        assertEquals(
            1,
            tokenAccounts.size
        )

        assertEquals(
            "4siT7XaV5xz6d1ArAy4ma8SkNRUhsrCQxjw9BpB8PfTK",
            tokenAccounts.firstOrNull()?.account?.data?.parsed?.info?.mint
        )

        assertEquals(
            "AQgMCXr8gcbjKmiB525YzcqQdVEZtAYVeBDWyDEBg9Z3",
            tokenAccounts.firstOrNull()?.account?.data?.parsed?.info?.owner
        )

        assertEquals(
            "E8qML25hDPZ1XWv9MP3pQ28dzY5oUmbbHUXVRoDuduUg",
            tokenAccounts.firstOrNull()?.pubkey
        )
    }

    @Test
    fun get_token_accounts_by_owner_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetTokenAccountsByOwnerRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getTokenAccountsByOwner(
                address = "AQgMCXr8gcbjKmiB525YzcqQdVEZtAYVeBDWyDEBg9Z3"
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
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32602,
            responseErrorCode
        )
    }

    @Test
    fun get_account_info_did_succeed() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetAccountInfoRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository
                .getAccountInfo(
                    address = "E8qML25hDPZ1XWv9MP3pQ28dzY5oUmbbHUXVRoDuduUg"
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

        assertTrue(success.result.result is Result.GetAccountInfoResult)

        val result = success.result.result as Result.GetAccountInfoResult
        val accountInfo = result.getAccountInfoResult

        assertEquals(
            "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
            accountInfo.owner
        )

        assertEquals(
            "OZD4GarKfuP80i1oVZ+uPPsiSLQum2MYash6e/sezy6Lygg7qRgpImK0ZuDjFIiW7iDjlcjbXakQr+qSUgkkOgEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            accountInfo.data?.first()
        )

        assertEquals(
            "base64",
            accountInfo.data?.last()
        )
    }

    @Test
    fun get_account_info_did_error() {
        solanaRPCApi = SolanaRPCApi(
            MockHttpClientFactoryImpl(
                GetAccountInfoRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaRPCRepository = SolanaRPCApiRepositoryImpl(solanaRPCApi)

        val emissions =
            mutableListOf<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getAccountInfo(
                address = "E8qML25hDPZ1XWv9MP3pQ28dzY5oUmbbHUXVRoDuduUg"
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
        val responseErrorCode = error.error?.error?.code

        assertEquals(
            -32602,
            responseErrorCode
        )
    }
}