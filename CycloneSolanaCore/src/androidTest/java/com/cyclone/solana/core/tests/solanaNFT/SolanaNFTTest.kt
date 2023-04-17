package com.cyclone.solana.core.tests.solanaNFT

import com.cyclone.solana.core.datamodel.dto.metaplex.meta_data.MetaplexMetaData
import com.cyclone.solana.core.http.client.MockHttpClientFactoryImpl
import com.cyclone.solana.core.http.dispatcher.GetNFTMetaDataRequestHandler
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.SolanaNFTApi
import com.cyclone.solana.core.repository.implementation.SolanaNFTRepositoryImpl
import com.cyclone.solana.core.repository.interfaces.SolanaNFTRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SolanaNFTTest {
    private lateinit var solanaNFTApi: SolanaNFTApi
    private lateinit var solanaNFTRepository: SolanaNFTRepository

    @Test
    fun get_nft_data_did_succeed() {
        solanaNFTApi = SolanaNFTApi(
            MockHttpClientFactoryImpl(
                GetNFTMetaDataRequestHandler.getSuccessResponse()
            ).createOkHttpClient()
        )

        solanaNFTRepository = SolanaNFTRepositoryImpl(solanaNFTApi)

        val emissions =
            mutableListOf<NetworkResource<MetaplexMetaData, Nothing>>()

        runBlocking {
            solanaNFTRepository
                .getNFTMetaData(
                    url = "https://arweave.net/e3Z0vssFdo7jSS7879q3w_-S14kNofs9t9X7jcGxCg8"
                )
                .collect {
                    emissions.add(it)
                }
        }

        Assert.assertEquals(2, emissions.size)

        Assert.assertNotNull(emissions[0])
        Assert.assertNotNull(emissions[1])

        Assert.assertTrue(emissions[0] is NetworkResource.Loading)
        Assert.assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success

        val result = success.result

        Assert.assertEquals(
            "Sol Otters #7",
            result.name
        )

        Assert.assertEquals(
            "#7 of Solana Otters NFT Collection.",
            result.description
        )

        Assert.assertEquals(
            "OTTER",
            result.symbol
        )

        Assert.assertEquals(
            "https://arweave.net/oJne_596sVqQsEwZum2IHfMEfmwUT_wvhthLsCU_0Tk?ext=png",
            result.image
        )
    }

    @Test
    fun get_account_info_did_error() {
        solanaNFTApi = SolanaNFTApi(
            MockHttpClientFactoryImpl(
                GetNFTMetaDataRequestHandler.getErrorResponse()
            ).createOkHttpClient()
        )

        solanaNFTRepository = SolanaNFTRepositoryImpl(solanaNFTApi)

        val emissions =
            mutableListOf<NetworkResource<MetaplexMetaData, Nothing>>()

        runBlocking {
            solanaNFTRepository.getNFTMetaData(
                url = "https://arweave.net/blah"
            ).collect {
                emissions.add(it)
            }
        }

        Assert.assertEquals(2, emissions.size)

        Assert.assertNotNull(emissions[0])
        Assert.assertNotNull(emissions[1])

        Assert.assertTrue(emissions[0] is NetworkResource.Loading)
        Assert.assertTrue(emissions[1] is NetworkResource.Error)
    }
}