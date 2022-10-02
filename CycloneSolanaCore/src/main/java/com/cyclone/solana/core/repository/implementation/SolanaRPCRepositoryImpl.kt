package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.constants.RPC
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCRequest
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.network.NetworkBoundResourceProvider
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.interfaces.SolanaRPCApi
import com.cyclone.solana.core.repository.interfaces.SolanaRPCRepository
import com.cyclone.solana.core.util.ExceptionUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class SolanaRPCRepositoryImpl(val solanaRPCApi: SolanaRPCApi): SolanaRPCRepository {
    override suspend fun getBalance(): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestBlockHash(): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> {
        val rpcRequest = RPCRequest(
            method = RPC.RPCMethods.GET_LATEST_BLOCKHASH,
            params = listOf(
                JsonObject().apply {
                    addProperty("commitment", "processed")
                }
            )
        )

        val networkBoundResourceProvider = object : NetworkBoundResourceProvider<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse> {
            override suspend fun loadFromNetwork(): Response<*> {
                return solanaRPCApi.getLatestBlockhash(rpcRequest)
            }

            override suspend fun isError(response: Response<*>?): Boolean {
                if (response?.isSuccessful != true)
                    return true

                val body = response.body()

                return body !is RPCResponse.Response || body.result == null
            }

            override suspend fun onError(response: Response<*>?): RPCResponse.ErrorResponse? {
                return ExceptionUtil.tryOrDefault(null) {
                    val serialise = Gson().toJson(response?.body(), RPCResponse.Response::class.java)
                    return@tryOrDefault Gson().fromJson(serialise, RPCResponse.ErrorResponse::class.java)
                }
            }

            override suspend fun parseNetworkResult(response: Response<*>?): RPCResponse.SuccessResponse? {
                return ExceptionUtil.tryOrDefault(null) {
                    val serialise = Gson().toJson(response?.body(), RPCResponse.Response::class.java)
                    return@tryOrDefault Gson().fromJson(serialise, RPCResponse.SuccessResponse::class.java)
                }
            }
        }

        return networkBoundResourceProvider.execute()
    }

    override suspend fun transferLamports(): Flow<NetworkResource<RPCResponse.SuccessResponse, RPCResponse.ErrorResponse>> {
        TODO("Not yet implemented")
    }
}