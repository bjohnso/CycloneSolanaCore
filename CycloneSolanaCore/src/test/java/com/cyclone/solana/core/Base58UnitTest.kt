package com.cyclone.solana.core

import com.cyclone.solana.core.extensions.hexToByteArray
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.Base58Encoder
import org.junit.Test
import org.junit.Assert.*

class Base58UnitTest {
    @Test
    fun `encode_to_base_58_is_correct`() {
        val expected = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"

        val hex = "BD284FDE1526B7B936A76BDE6D8955B6617E56A01E5D0309DEDA3C426E0A7CA0"
        val byteArray = hex.hexToByteArray()

        assertEquals(
            expected,
            Base58Encoder.invoke(byteArray)
        )
    }

    @Test
    fun `decode_to_from_58_is_correct`() {
        val hex = "BD284FDE1526B7B936A76BDE6D8955B6617E56A01E5D0309DEDA3C426E0A7CA0"
        val expected = hex.hexToByteArray()

        val base58 = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"

        assertArrayEquals(
            expected,
            Base58Decoder.invoke(base58)
        )
    }
}