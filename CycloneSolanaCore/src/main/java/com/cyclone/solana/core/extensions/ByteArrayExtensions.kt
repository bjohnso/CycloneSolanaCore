package com.cyclone.solana.core.extensions

import com.cyclone.solana.core.usecase.Base58Encoder
import java.math.BigInteger

fun ByteArray.toBigInteger() = BigInteger(1, this)

fun ByteArray.toBinaryString(): String = BigInteger(1, this).toString(2)

fun IntArray.toByteArray(): ByteArray = this.foldIndexed(
    ByteArray(this.size)
) { i, a, v -> a.apply { set(i, v.toByte()) } }

fun List<ByteArray>.flatten() = this.reduceOrNull { acc, bytes ->
    acc + bytes
} ?: ByteArray(0)

fun ByteArray.addModulo256(other: ByteArray): ByteArray {
    val result = ByteArray(size)
    var carry = 0
    for (i in lastIndex downTo 0) {
        val sum = (this[i].toInt() and 0xFF) + (other[i].toInt() and 0xFF) + carry
        result[i] = sum.toByte()
        carry = sum ushr 8
    }
    return result
}

fun ByteArray.scalarMultiply(scalar: ByteArray): ByteArray {
    val result = ByteArray(size)
    val a = this.copyOf().apply { reverse() }
    val b = scalar.copyOf().apply { reverse() }

    for (i in a.indices) {
        var carry = 0
        for (j in b.indices) {
            val product = (a[i].toInt() and 0xFF) * (b[j].toInt() and 0xFF) + (result[i + j].toInt() and 0xFF) + carry
            result[i + j] = product.toByte()
            carry = product ushr 8
        }
    }

    result.reverse()
    return result
}

fun ByteArray.removeLeadingZeros(): ByteArray {
    var i = 0
    while (i < this.size && this[i] == 0.toByte()) {
        i++
    }
    return this.copyOfRange(i, this.size)
}

fun ByteArray.toBase58(): String {
    return Base58Encoder.invoke(this)
}