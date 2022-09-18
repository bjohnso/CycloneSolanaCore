package com.cyclone.solana.core.extensions

import java.math.BigInteger

fun String.binaryToHex(): String = Integer.toHexString(
    Integer.parseInt(this, 2)
)

fun String.hexToByteArray(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}