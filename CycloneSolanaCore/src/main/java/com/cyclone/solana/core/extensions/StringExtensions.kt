package com.cyclone.solana.core.extensions

fun String.binaryToHex(): String = Integer.toHexString(
    Integer.parseInt(this, 2)
)