package com.cyclone.solana.core.repository.interfaces

import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCErrorResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.network.NetworkResource
import kotlinx.coroutines.flow.Flow

interface SolanaRPCRepository {
    suspend fun getBalance(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>>
    suspend fun getLatestBlockHash(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>>
    suspend fun transferLamports(): Flow<NetworkResource<RPCResponse, RPCErrorResponse>>
}