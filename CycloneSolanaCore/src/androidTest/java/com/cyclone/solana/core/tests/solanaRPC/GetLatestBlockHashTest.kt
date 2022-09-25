package com.cyclone.solana.core.tests.solanaRPC

import com.cyclone.solana.core.http.dispatcher.LatestBlockhashDispatcher
import org.junit.Test

class GetLatestBlockHashTest: BaseSolanaRPCTest() {
    @Test
    fun get_blockhash_is_correct() {
        mockWebServer.dispatcher = LatestBlockhashDispatcher.getSuccessResponse()
    }
}