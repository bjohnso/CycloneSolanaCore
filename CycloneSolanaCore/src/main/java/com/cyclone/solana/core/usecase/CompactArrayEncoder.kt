package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.extensions.*

object CompactArrayEncoder {
    operator fun invoke(byteArray: ByteArray): ByteArray {
        val firstByteThreshold = 0x7f
        val secondByteThreshold = 0x3fff

        val number = byteArray.toInteger()
        val binaryString = number.toBinaryString()
            .takeLast(16)
            .padStart(16, '0')

        return when {
            number > secondByteThreshold -> {
                listOf(
                    "1${binaryString.slice(9 until 16)}"
                        .binaryToByteArray(),
                    "1${binaryString.slice(2 until 9)}"
                        .binaryToByteArray(),
                    "0${binaryString.slice(0 until 2)}"
                        .binaryToByteArray(),
                )
            }
            number > firstByteThreshold -> {
                listOf(
                    "1${binaryString.slice(9 until 16)}"
                        .binaryToByteArray(),
                    "0${binaryString.slice(2 until 9)}"
                        .binaryToByteArray(),
                )
            }
            else -> {
                listOf(
                    "0${binaryString.slice(9 until 16)}"
                        .binaryToByteArray(),
                )
            }
        }.flatten()
    }
}