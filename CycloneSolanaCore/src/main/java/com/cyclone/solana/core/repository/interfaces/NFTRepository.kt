package com.cyclone.solana.core.repository.interfaces

import com.cyclone.solana.core.datamodel.dto.metaplex.meta_data.MetaplexMetaData
import com.cyclone.solana.core.network.NetworkResource
import kotlinx.coroutines.flow.Flow

interface NFTRepository {
    suspend fun getNFTMetaData(url: String): Flow<NetworkResource<MetaplexMetaData, Nothing>>
}