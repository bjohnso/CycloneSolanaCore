package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.datamodel.dto.solanaRPC.request.RPCRequest
import com.cyclone.solana.core.datamodel.dto.solanaRPC.response.RPCResponse
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.SolanaRPCApi
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SolanaRPCRepositoryImpl(private val solanaRpcApi: SolanaRPCApi): SolanaRPCRepository {
    override suspend fun getBalance(
        address: String
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestGetBalance(address = address)
        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.getBalance(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) { /* Ignore */ }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }

    override suspend fun getLatestBlockHash(): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestLatestBlockhash()

        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.getLatestBlockhash(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) {
            println(
                "getLatestBlockHash: ${e.message}"
            )
        }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }

    override suspend fun sendTransaction(
        vararg transaction: String
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestSendTransaction(
            transactions = transaction.toList()
        )

        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.sendTransaction(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) { /* Ignore */ }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }

    override suspend fun getTransaction(
        transactionSignature: String
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestGetTransaction(
            transactionSignature = transactionSignature,
        )

        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.getTransaction(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) { /* Ignore */ }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }

    override suspend fun getTokenAccountsByOwner(
        address: String,
        mint: String,
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestGetTokenAccountsByOwner(
            address = address,
            mint = mint,
        )

        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.getTokenAccountsByOwner(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) { /* Ignore */ }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }

    override suspend fun getAccountInfo(
        address: String
    ): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> = flow {
        val rpcRequest = RPCRequest.RequestGetAccountInfo(
            address = address,
        )

        emit(
            NetworkResource.Loading
        )

        try {
            when (val response = solanaRpcApi.getAccountInfo(rpcRequest)) {
                is RPCResponse.SuccessResponse -> {
                    emit(
                        NetworkResource.Success(response)
                    )

                    return@flow
                }
                is RPCResponse.ErrorResponse -> {
                    emit(
                        NetworkResource.Error(response)
                    )

                    return@flow
                }
            }
        }
        catch (c: CancellationException) { throw c }
        catch (e: Exception) { /* Ignore */ }

        emit(
            NetworkResource.Error(null)
        )

        return@flow
    }
}