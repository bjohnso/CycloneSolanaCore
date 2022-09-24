package com.cyclone.solana.core

import com.cyclone.solana.core.extensions.flatten
import com.cyclone.solana.core.extensions.hexToByteArray
import com.cyclone.solana.core.usecase.CompactArrayEncoder
import org.junit.Test

import org.junit.Assert.*
import java.nio.ByteBuffer

class CompactArrayUnitTest {
    @Test
    fun `encode_to_1_bytes_is_correct`() {
        evaluateInputs(
            hashMapOf(
                Pair(0x0000, listOf("0")),
                Pair(0x0001, listOf("1")),
                Pair(0x007f, listOf("7f")),
            )
        )
    }

    @Test
    fun `encode_to_2_bytes_is_correct`() {
        evaluateInputs(
            hashMapOf(
                Pair(0x0080, listOf("80", "1")),
                Pair(0x3fff, listOf("ff", "7f")),
            )
        )
    }

    @Test
    fun `encode_to_3_bytes_is_correct`() {
       evaluateInputs(
           hashMapOf(
               Pair(0x4000, listOf("80", "80", "1")),
               Pair(0xc000, listOf("80", "80", "3")),
               Pair(0xffff, listOf("ff", "ff", "3")),
           )
       )
    }

    private fun evaluateInputs(inputs: HashMap<Int, List<String>>) {
        for (input in inputs) {
            val expected = input.value.map {
                it.hexToByteArray()
            }.flatten()

            val bytes = ByteBuffer
                .allocate(Int.SIZE_BYTES)
                .putInt(input.key)
                .array()

            val result = CompactArrayEncoder.invoke(bytes)

            assertArrayEquals(
                expected.toTypedArray(),
                result.toTypedArray(),
            )
        }
    }
}