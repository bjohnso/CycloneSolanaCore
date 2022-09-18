package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.extensions.binaryToHex
import com.cyclone.solana.core.extensions.toBinaryString
import com.cyclone.solana.core.extensions.toInteger

object CompactArrayEncoder {
    operator fun invoke(byteArray: ByteArray): List<String> {
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
                        .binaryToHex(),
                    "1${binaryString.slice(2 until 9)}"
                        .binaryToHex(),
                    "0${binaryString.slice(0 until 2)}"
                        .binaryToHex(),
                )
            }
            number > firstByteThreshold -> {
                listOf(
                    "1${binaryString.slice(9 until 16)}"
                        .binaryToHex(),
                    "0${binaryString.slice(2 until 9)}"
                        .binaryToHex(),
                )
            }
            else -> {
                listOf(
                    "0${binaryString.slice(9 until 16)}"
                        .binaryToHex(),
                )
            }
        }
    }
}