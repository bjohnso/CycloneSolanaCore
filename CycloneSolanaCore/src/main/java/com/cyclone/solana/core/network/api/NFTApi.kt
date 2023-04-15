package com.cyclone.solana.core.network.api

import com.cyclone.solana.core.datamodel.dto.metaplex.meta_data.MetaplexMetaData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlin.text.get

class NFTApi(private val client: HttpClient) {
    suspend fun getNFTMetaData(url: String): MetaplexMetaData = client.get {
        url(url)
    }.body()
}