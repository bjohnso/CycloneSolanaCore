package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.extensions.*
import com.cyclone.solana.core.usecase.MnemonicDecoder
import com.cyclone.solana.core.usecase.MnemonicEncoder
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class MnemonicUnitTest {

    @Test
    fun encode_to_12_word_mnemonic_is_correct() {
        val hexSeed = "2e5a2fb1ecf80b8995b82a2801dc6d03"

        val expected = listOf(
            "comic",
            "sphere",
            "unaware",
            "supreme",
            "level",
            "shadow",
            "finger",
            "aim",
            "chimney",
            "auction",
            "brave",
            "alter"
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
    fun decode_from_12_word_mnemonic_is_correct() {
        val expected = "2e5a2fb1ecf80b8995b82a2801dc6d03".hexToByteArray()

        val result = MnemonicDecoder.invoke(
            listOf(
                "comic",
                "sphere",
                "unaware",
                "supreme",
                "level",
                "shadow",
                "finger",
                "aim",
                "chimney",
                "auction",
                "brave",
                "alter"
            )
        )

        assertArrayEquals(expected, result)
    }

    @Test
    fun decode_from_12_word_mnemonic_is_incorrect() {
        val expected = null

        val result = MnemonicDecoder.invoke(
            listOf(
                "comc",
                "sphere",
                "unaware",
                "spreme",
                "lel",
                "shaow",
                "figer",
                "aim",
                "chimey",
                "aution",
                "brave",
                "aler"
            )
        )

        assertArrayEquals(expected, result)
    }
}