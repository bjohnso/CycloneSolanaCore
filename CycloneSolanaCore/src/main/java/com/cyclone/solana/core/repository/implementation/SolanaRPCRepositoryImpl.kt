package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.constants.RPC
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCErrorResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCRequest
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.network.NetworkBoundResourceProvider
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.interfaces.SolanaRPCApi
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class SolanaRPCRepositoryImpl(val solanaRPCApi: SolanaRPCApi): SolanaRPCRepository {
    override suspend fun getBalance(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestBlockHash(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>> {
        val rpcRequest = RPCRequest(
            method = RPC.RPCMethods.GET_LATEST_BLOCKHASH,
            params = listOf(
                JsonObject().apply {
                    addProperty("commitment", "processed")
                }
            )
        )

        val networkBoundResourceProvider = object : NetworkBoundResourceProvider<RPCResponse, RPCErrorResponse> {
            override suspend fun loadFromNetwork(): Response<*> {
                return solanaRPCApi.getLatestBlockhash(rpcRequest)
            }

            override suspend fun onError(response: Response<*>?): RPCErrorResponse? {
                return response?.errorBody()?.charStream()?.let {
                    Gson().fromJson(it, RPCErrorResponse::class.java)
                }
            }

            override suspend fun parseNetworkResult(response: Response<*>?): RPCResponse {
                return response?.body() as RPCResponse
            }
        }

        return networkBoundResourceProvider.execute()
    }

    override suspend fun transferLamports(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>> {
        TODO("Not yet implemented")
    }
}