package com.cyclone.solana.core.network

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response

interface NetworkBoundResourceProvider<ResultType : Any, ErrorType: Any> {

    @WorkerThread
    suspend fun loadFromNetwork(): Response<*>

    @WorkerThread
    suspend fun parseNetworkResult(response: Response<*>?): ResultType?

    suspend fun isError(response: Response<*>?): Boolean {
        return response?.isSuccessful == true
    }

    @WorkerThread
    fun loadFromCache(): Flow<ResultType>? {
        return null // cache disabled by default
    }

    @WorkerThread
    suspend fun cacheNetworkResult(result: ResultType?): ResultType? {
        return null // cache disabled by default
    }

    @WorkerThread
    fun validateCacheData(data: ResultType): Boolean {
        return false // cache disabled by default
    }

    @WorkerThread
    suspend fun onError(response: Response<*>?): ErrorType?

    /**
     * By default execute() will first check the cache for data
     * If data IS found in the cache it will emit the data with a NetworkResource.Cached()
     * If data IS NOT found in the cache it will emit a NetworkResource.Loading() before making
     * a network request and finally emitting either a success NetworkResource.Success() or
     * a NetworkResource.Error() with the appropriate response data in both cases
     *
     * This method can be overridden with a completely custom implementation if required
     */
    fun execute(): Flow<NetworkResource<ResultType, ErrorType>> = flow {
        val cacheData = loadFromCache()?.first()

        if (cacheData != null && validateCacheData(cacheData)) {
            emit(NetworkResource.Cached(cacheData))
        } else {
            emit(NetworkResource.Loading)
            val networkData = loadFromNetwork()

            if (!isError(networkData)) {
                val parsed = parseNetworkResult(networkData)
                if (parsed != null) {
                    cacheNetworkResult(parsed)
                    emit(NetworkResource.Success(parsed))
                    return@flow
                }
            }

            emit(NetworkResource.Error(onError(networkData)))

            return@flow
        }
    }
}