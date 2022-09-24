package com.cyclone.solana.core.extensions

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.toBinaryString(): String {
    return Integer.toBinaryString(this)
}

fun Int.toLEByteArray(byteSize: Int): ByteArray {
    return ByteBuffer
        .allocate(byteSize)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt(this)
        .array()
}