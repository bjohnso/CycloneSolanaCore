package com.cyclone.solana.core.extensions

import com.cyclone.solana.core.usecase.Base58Decoder
import java.math.BigInteger

fun String.binaryToHex(): String = BigInteger(this, 2).toString(16)

fun String.hexToBinary(): String = BigInteger(this, 16).toString(2)

fun String.hexToByteArray(): ByteArray {
    var string = this

    if (length % 2 != 0) {
        string = "0${string}"
    }

    return string.chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}

fun String.binaryToByteArray(): ByteArray {
    val bytes = this.chunked(8).map {
        BigInteger(it.binaryToHex(), 16).toByte()
    }

    return bytes.toByteArray()
}

fun String.base58ToByteArray() = Base58Decoder.invoke(this)