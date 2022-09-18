package com.cyclone.solana.core.extensions

import java.util.*

fun String.binaryToHex(): String = Integer.toHexString(
    Integer.parseInt(this, 2)
)

fun String.hexToByteArray(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun String.binaryToByteArray(): ByteArray {
    val binary = this

    return BitSet(binary.length).apply {
        for (i in binary.indices) {
            this[i] = when(binary[i]) {
                '1' -> true
                else -> false
            }
        }
    }.toByteArray()
}