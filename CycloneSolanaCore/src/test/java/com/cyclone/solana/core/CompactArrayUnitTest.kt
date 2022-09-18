package com.cyclone.solana.core

import com.cyclone.solana.core.usecase.CompactArrayEncoder
import org.junit.Test

import org.junit.Assert.*
import java.nio.ByteBuffer

class CompactArrayUnitTest {
    @Test
    fun `1_bytes_is_correct`() {

        val inputs = hashMapOf(
            Pair(0x0000, listOf("0")),
            Pair(0x0001, listOf("1")),
            Pair(0x007f, listOf("7f")),
        )

        for (input in inputs) {
            val bytes = ByteBuffer
                .allocate(Int.SIZE_BYTES)
                .putInt(input.key)
                .array()

            val result = CompactArrayEncoder.invoke(bytes)

            assertArrayEquals(
                input.value.toTypedArray(),
                result.toTypedArray(),
            )
        }
    }

    @Test
    fun `2_bytes_is_correct`() {

        val inputs = hashMapOf(
            Pair(0x0080, listOf("80", "1")),
            Pair(0x3fff, listOf("ff", "7f")),
        )

        for (input in inputs) {
            val bytes = ByteBuffer
                .allocate(Int.SIZE_BYTES)
                .putInt(input.key)
                .array()

            val result = CompactArrayEncoder.invoke(bytes)

            assertArrayEquals(
                input.value.toTypedArray(),
                result.toTypedArray(),
            )
        }
    }

    @Test
    fun `3_bytes_is_correct`() {

        val inputs = hashMapOf(
            Pair(0x4000, listOf("80", "80", "1")),
            Pair(0xc000, listOf("80", "80", "3")),
            Pair(0xffff, listOf("ff", "ff", "3")),
        )

        for (input in inputs) {
            val bytes = ByteBuffer
                .allocate(Int.SIZE_BYTES)
                .putInt(input.key)
                .array()

            val result = CompactArrayEncoder.invoke(bytes)

            assertArrayEquals(
                input.value.toTypedArray(),
                result.toTypedArray(),
            )
        }
    }
}