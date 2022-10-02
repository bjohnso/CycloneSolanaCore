package com.cyclone.solana.core.network.api.interfaces

import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCRequest
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SolanaRPCApi {
    @POST("/")
    suspend fun getBalance(
        @Body body: RPCRequest
    ): Response<RPCResponse>

    @POST("/")
    suspend fun getLatestBlockhash(
        @Body body: RPCRequest
    ): Response<RPCResponse>

    @POST("/")
    suspend fun transferLamports(
        @Body body: RPCRequest
    ): Response<RPCResponse>
}