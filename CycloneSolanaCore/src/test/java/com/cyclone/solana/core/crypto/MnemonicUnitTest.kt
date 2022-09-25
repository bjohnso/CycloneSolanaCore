package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.extensions.*
import com.cyclone.solana.core.usecase.MnemonicDecoder
import com.cyclone.solana.core.usecase.MnemonicEncoder
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class MnemonicUnitTest {

    @Test
    fun encode_to_12_word_mnemonic_is_correct() {
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
    fun decode_from_12_word_mnemonic_is_correct() {
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