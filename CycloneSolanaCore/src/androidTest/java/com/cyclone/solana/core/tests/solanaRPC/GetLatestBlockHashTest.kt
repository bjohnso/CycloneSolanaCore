package com.cyclone.solana.core.tests.solanaRPC

import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCErrorResponse
import com.cyclone.solana.core.datamodel.dto.solanaRPC.RPCResponse
import com.cyclone.solana.core.http.dispatcher.LatestBlockhashDispatcher
import com.cyclone.solana.core.network.NetworkResource
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetLatestBlockHashTest: BaseSolanaRPCTest() {
    @Test
    fun get_blockhash_is_correct() {
        mockWebServer.dispatcher = LatestBlockhashDispatcher.getSuccessResponse()

        val emissions = mutableListOf<NetworkResource<RPCResponse, RPCErrorResponse>>()

        runBlocking {
            solanaRPCRepository.getLatestBlockHash().collect {
                emissions.add(it)
            }
        }

        assertEquals(2, emissions.size)

        assertNotNull(emissions[0])
        assertNotNull(emissions[1])

        assertTrue(emissions[0] is NetworkResource.Loading)
        assertTrue(emissions[1] is NetworkResource.Success)

        val success = emissions[1] as NetworkResource.Success
        val responseBlockhash = success.result.result.value
            .getAsJsonPrimitive("blockhash")
            .asString

        assertEquals(
            "GpTSZjernhsXHeKG7K5zuBtojwkGnz9zRgcWH45zHkHb",
            responseBlockhash
        )
    }
}