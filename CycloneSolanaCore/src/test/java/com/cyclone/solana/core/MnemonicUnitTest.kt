package com.cyclone.solana.core

import com.cyclone.solana.core.constants.Mnemonic
import com.cyclone.solana.core.extensions.*
import com.cyclone.solana.core.usecase.MnemonicDecoder
import com.cyclone.solana.core.usecase.MnemonicEncoder
import com.cyclone.solana.core.util.ExceptionUtil
import com.cyclone.solana.core.util.Log
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger
import java.security.MessageDigest

class MnemonicUnitTest {

    @Test
    fun `encode_to_12_word_mnemonic_is_correct`() {
        val hexSeed = "b35cd271ce77c440d5aac7fd591403e3"

        val expected = listOf(
            "rebuild",
            "track",
            "organ",
            "ostrich",
            "labor",
            "call",
            "find",
            "flight",
            "wool",
            "silly",
            "abstract",
            "shoulder"
        )

        val result = MnemonicEncoder.invoke(
            hexSeed.hexToByteArray()
        )

        assertArrayEquals(
            expected.toTypedArray(),
            result?.toTypedArray()
        )
    }

    @Test
    fun `decode_from_12_word_mnemonic_is_correct`() {
        val expected = "b35cd271ce77c440d5aac7fd591403e3".hexToByteArray()

        val result = MnemonicDecoder.invoke(
            listOf(
                "rebuild",
                "track",
                "organ",
                "ostrich",
                "labor",
                "call",
                "find",
                "flight",
                "wool",
                "silly",
                "abstract",
                "shoulder"
            )
        )

        assertArrayEquals(expected, result)
    }
}