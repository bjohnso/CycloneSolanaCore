package com.cyclone.solana.core.network

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response

interface NetworkBoundResource<ResultType : Any> {

    @WorkerThread
    fun loadFromCache(): Flow<ResultType>?

    @WorkerThread
    suspend fun loadFromNetwork(): Response<*>?

    @WorkerThread
    fun validateCacheData(data: ResultType?): Boolean

    @WorkerThread
    suspend fun cacheNetworkResult(response: Response<*>?): ResultType

    @WorkerThread
    suspend fun onError(response: Response<*>?): ResultType

    /**
     * By default execute() will first check the cache for data
     * If data IS found in the cache it will emit the data with a NetworkResource.Cached()
     * If data IS NOT found in the cache it will emit a NetworkResource.Loading() before making
     * a network request and finally emitting either a success NetworkResource.Success() or
     * a NetworkResource.Error() with the appropriate response data in both cases
     *
     * This method can be overridden with a completely custom implementation if required
     */
    fun execute(): Flow<NetworkResource<ResultType, ResultType>> = flow {
        val cacheData = loadFromCache()?.first()

        if (cacheData != null && validateCacheData(cacheData)) {
            emit(NetworkResource.Cached(cacheData))
        } else {
            emit(NetworkResource.Loading)
            val networkData = loadFromNetwork()

            when (networkData?.isSuccessful) {
                true -> emit(
                    NetworkResource.Success(cacheNetworkResult(networkData))
                )
                else -> emit(
                    NetworkResource.Error(onError(networkData))
                )
            }
        }
    }
}