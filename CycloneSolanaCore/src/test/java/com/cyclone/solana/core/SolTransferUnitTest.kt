package com.cyclone.solana.core

import com.cyclone.solana.core.constants.Units
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.SolTransfer
import com.cyclone.solana.core.util.Log
import org.junit.Assert
import org.junit.Test

class SolTransferUnitTest {
    @Test
    fun `transfer_transaction_is_correct`() {
        val expectedUnsigned = Base58Decoder.invoke(
            "87PYuLJkbJfgFnkDWHsaURDReR7xSajZCnNdUEXtPTPmsxGFzLqN9NvGWVK8QxgCEsgp7QtMiAHGgHfVP5h4K4uhpUyZwBoZaV12jAunHwT3Wi9MBG9kUE1NHEQJceUCTvPNsJqAGi6emQ3KPKgNwYQ4kP3ob9YRDjQF83nGmKW9KdZzKP3H36nyY4DYYFD4tXenZA6iHP7V"
        )

        val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
        val toAddress = "W5RJACTF8t4TC1LSBr4SXqkuZkFMM8yZFvRqqfM6tqM"
        val blockhash = "E1fZFLAd1aB8xUG6mx4KuEfG4kNKXBY99W26o5RPbbvk"
        val lamports = Units.LAMPORTS_PER_SOL

        val result = SolTransfer.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        )

        Assert.assertArrayEquals(
            expectedUnsigned,
            result.serialise()
        )
    }
}