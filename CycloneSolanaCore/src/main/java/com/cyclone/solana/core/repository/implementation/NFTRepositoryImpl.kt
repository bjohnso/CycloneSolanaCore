package com.cyclone.solana.core.repository.implementation

import android.util.Log
import com.cyclone.solana.core.datamodel.dto.metaplex.meta_data.MetaplexMetaData
import com.cyclone.solana.core.datamodel.dto.solanaRPC.response.RPCResponse
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.NFTApi
import com.cyclone.solana.core.repository.interfaces.NFTRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NFTRepositoryImpl(private val nftApi: NFTApi): NFTRepository {
    override suspend fun getNFTMetaData(url: String): Flow<NetworkResource<MetaplexMetaData, Nothing>> {
        return flow {
            emit(
                NetworkResource.Loading
            )

            try {

                Log.e("TEST_ME", "getNFTMetaData: $url")

                emit(
                    NetworkResource.Success(nftApi.getNFTMetaData(url))
                )

                return@flow
            }
            catch (c: CancellationException) { throw c }
            catch (e: Exception) { /* Ignore */ }

            emit(
                NetworkResource.Error(null)
            )

            return@flow
        }
    }
}