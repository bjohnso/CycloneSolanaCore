package com.cyclone.solana.core.extensions

import java.math.BigInteger

fun ByteArray.toInteger() = BigInteger(this).toInt()