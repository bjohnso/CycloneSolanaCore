package com.cyclone.solana.core.repository.implementation

import com.cyclone.solana.core.datamodel.dto.metaplex.meta_data.MetaplexMetaData
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.SolanaNFTApi
import com.cyclone.solana.core.repository.interfaces.SolanaNFTRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SolanaNFTRepositoryImpl(private val solanaNftApi: SolanaNFTApi): SolanaNFTRepository {
    override suspend fun getNFTMetaData(url: String): Flow<NetworkResource<MetaplexMetaData, Nothing>> {
        return flow {
            emit(
                NetworkResource.Loading
            )

            try {
                emit(
                    NetworkResource.Success(solanaNftApi.getNFTMetaData(url))
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