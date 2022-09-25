package com.cyclone.solana.core.repository.interfaces

import com.cyclone.solana.core.datamodel.dto.network.ErrorResponse
import com.cyclone.solana.core.datamodel.entity.RPCResponse
import com.cyclone.solana.core.network.NetworkResource
import kotlinx.coroutines.flow.Flow

interface SolanaRPCRepository {
    suspend fun getBalance(): Flow<NetworkResource<RPCResponse, ErrorResponse>>
    suspend fun getLatestBlockHash(): Flow<NetworkResource<RPCResponse, ErrorResponse>>
    suspend fun transferLamports(): Flow<NetworkResource<RPCResponse, ErrorResponse>>
}