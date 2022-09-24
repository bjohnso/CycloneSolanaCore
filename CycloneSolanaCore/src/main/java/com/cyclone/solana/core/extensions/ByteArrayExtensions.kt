package com.cyclone.solana.core.extensions

import java.math.BigInteger

fun ByteArray.toInteger() = BigInteger(this).toInt()

fun ByteArray.toBinaryString(): String = BigInteger(1, this).toString(2)

fun List<ByteArray>.flatten() = this.reduceOrNull { acc, bytes ->
    acc + bytes
} ?: ByteArray(0)