package com.cyclone.solana.core.network

sealed class NetworkResource<out R : Any, out E: Any> {
    object Loading: NetworkResource<Nothing, Nothing>()
    data class Error<out E : Any>(val error: E): NetworkResource<Nothing, E>()
    data class Success<out R : Any>(val result: R) : NetworkResource<R, Nothing>()
    data class Cached<out R: Any>(val result: R) : NetworkResource<R, Nothing>()
}

