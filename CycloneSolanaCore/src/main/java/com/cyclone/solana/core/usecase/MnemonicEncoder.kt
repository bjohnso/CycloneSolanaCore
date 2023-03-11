package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Mnemonic
import com.cyclone.solana.core.extensions.toBinaryString
import java.math.BigInteger
import java.security.MessageDigest

object MnemonicEncoder {
    operator fun invoke(seed: ByteArray): List<String>? {
        val checksumLength = seed.size / 4
        val entropyLength = seed.size * 8

        val entropyBinaryString = seed
            .toBinaryString()
            .takeLast(entropyLength)
            .padStart(entropyLength, '0')

        val checksum = hashEntropy(
            seed
        )
            .toBinaryString()
            .padStart(256, '0')
            .take(checksumLength)

        val mnemonicBinaryString = "$entropyBinaryString$checksum"

        if (mnemonicBinaryString.length != entropyLength + checksumLength) {
            return null
        }

        return mnemonicBinaryString
            .chunked(11)
            .map {
                val index = BigInteger(it, 2).toInt()
                Mnemonic.Words.WORDS[index]
            }
    }

    private fun hashEntropy(entropy: ByteArray): ByteArray {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return messageDigest.digest(entropy)
    }
}