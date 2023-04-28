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

fun Int.toBEByteArray(byteSize: Int): ByteArray {
    return ByteBuffer
        .allocate(byteSize)
        .order(ByteOrder.BIG_ENDIAN)
        .putInt(this)
        .array()
}

fun Long.toLEByteArray(byteSize: Int): ByteArray {
    return ByteBuffer
        .allocate(byteSize)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putLong(this)
        .array()
}

fun Long.toBEByteArray(byteSize: Int): ByteArray {
    return ByteBuffer
        .allocate(byteSize)
        .order(ByteOrder.BIG_ENDIAN)
        .putLong(this)
        .array()
}

fun UInt.toBEByteArray(): ByteArray {
    return ByteBuffer
        .allocate(4)
        .order(ByteOrder.BIG_ENDIAN)
        .putInt(this.toInt())
        .array()
}