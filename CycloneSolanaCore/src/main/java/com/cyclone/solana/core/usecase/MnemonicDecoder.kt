package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Mnemonic
import com.cyclone.solana.core.extensions.binaryToByteArray
import com.cyclone.solana.core.extensions.toBinaryString
import com.cyclone.solana.core.util.ExceptionUtil
import java.math.BigInteger
import java.security.MessageDigest

object MnemonicDecoder {
    operator fun invoke(mnemonicList: List<String>): ByteArray? {
        val mnemonicWordList = Mnemonic.Words.WORDS

        val mnemonicBitLength = mnemonicList.size * 11
        val entropyLength = (mnemonicBitLength * 32) / 33

        var mnemonicBinaryString = ""

        ExceptionUtil.tryOrDefault(null) {
            mnemonicList
                .forEach {
                    val decimal = mnemonicWordList.indexOf(it)
                    require(decimal > -1)

                    val binaryString = BigInteger.valueOf(decimal.toLong())
                        .toString(2)
                        .takeLast(11)
                        .padStart(11, '0')

                    mnemonicBinaryString += binaryString
                }
        }

        if (mnemonicBinaryString.length != mnemonicBitLength) {
            return null
        }

        val checksumBinaryString = mnemonicBinaryString.takeLast(mnemonicBitLength - entropyLength)

        mnemonicBinaryString = mnemonicBinaryString.take(entropyLength)

        val entropy = mnemonicBinaryString.binaryToByteArray()

        return if (
            validateChecksumForEntropy(
                entropy = entropy,
                checksum = checksumBinaryString
            )
        ) entropy else null
    }

    private fun validateChecksumForEntropy(entropy: ByteArray, checksum: String): Boolean {
        val checksumLength = entropy.size / 4
        val hashedEntropy = hashEntropy(entropy)

        val hashedChecksum = hashedEntropy
            .toBinaryString()
            .takeLast(checksumLength)

        return checksum == hashedChecksum
    }

    private fun hashEntropy(entropy: ByteArray): ByteArray {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return messageDigest.digest(entropy)
    }
}