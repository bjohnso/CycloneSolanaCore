package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.extensions.binaryToByteArray
import com.cyclone.solana.core.extensions.toBinaryString
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.pow

object SeedDeriver {
    /**
     * Derives Ed25519 Seed using derivation path m/44'/501'
     */
    operator fun invoke(seed: ByteArray): List<AsymmetricCipherKeyPair> {
        val entropyLength = seed.size * 8

        val entropyBinaryString = seed
            .toBinaryString()
            .padStart(entropyLength)

        val entropy = entropyBinaryString.binaryToByteArray()

        val keyPairs = mutableListOf<AsymmetricCipherKeyPair>()

        val indices = List(66) {
            when (it) {
                0 -> 44
                1 -> 501
                else -> it - 2
            }
        }

        var hmac = hashSeed(
            seed = entropy,
            salt = "mnemonic".toByteArray()
        )

        var privateKey = hmac
            .toBinaryString()
            .padStart(512, '0')
            .take(256)

        var chainCode = hmac
            .toBinaryString()
            .padStart(512, '0')
            .takeLast(256)

        indices.forEachIndexed { i, item ->
            val index = (2.toDouble().pow(31) + item)
                .toBigDecimal()
                .toBigIntegerExact()
                .toString(2)

            val message = (privateKey + index)
                .padStart(296, '0')

            hmac = hashSeed(
                seed = message.binaryToByteArray(),
                salt = chainCode.binaryToByteArray()
            )

            val entropy = hmac
                .toBinaryString()
                .padStart(512, '0')
                .take(256)

            privateKey = deriveChildPrivateKey(
                childEntropy = entropy,
                parentKey = chainCode
            )

            chainCode = hmac
                .toBinaryString()
                .padStart(512, '0')
                .takeLast(256)

            if (i > 1) {
                val keyPair = getEd25519KeyPairFromPrivateKey(
                    privateKey.binaryToByteArray()
                )

                keyPairs.add(keyPair)
            }
        }

        return keyPairs
    }

    private fun getEd25519KeyPairFromPrivateKey(byteArray: ByteArray): AsymmetricCipherKeyPair {
        val privateKey = Ed25519PrivateKeyParameters(byteArray)
        val publicKey = privateKey.generatePublicKey()

        return AsymmetricCipherKeyPair(
            publicKey, privateKey
        )
    }

    private fun hashSeed(seed: ByteArray, salt: ByteArray? = null): ByteArray {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        if (salt != null) {
            messageDigest.update(salt)
        }

        return messageDigest.digest(seed)
    }

    private fun deriveChildPrivateKey(childEntropy: String, parentKey: String): String {
        val ed25519Order = BigInteger(
            "10000000D73E37DB0F1F98CCBE59434D5FAA8B4D70C9B0800000000000000000",
            16
        )

        val key = BigInteger(childEntropy, 2) + BigInteger(parentKey, 2)
        val result = key % ed25519Order
        return result.toString(2).padStart(256, '0')
    }
}