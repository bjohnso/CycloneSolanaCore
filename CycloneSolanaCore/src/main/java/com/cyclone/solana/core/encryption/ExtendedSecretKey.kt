package com.cyclone.solana.core.encryption

import com.cyclone.solana.core.datamodel.dto.keypair.SolanaKeyPair
import com.cyclone.solana.core.extensions.toBEByteArray
import org.bouncycastle.crypto.Mac
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.KeyParameter

data class ExtendedSecretKey(
    val secretKey: ByteArray, // 32 bytes
    val chainCode: ByteArray, // 32 bytes
    val depth: Int = 0,
    val childIndex: Int = 0
) {
    fun derive(path: List<Int>): Result<ExtendedSecretKey> {
        var next: ExtendedSecretKey = if (path.isNotEmpty()) {
           deriveChild(path[0]).getOrThrow()
        } else this

        for (index in path.drop(1)) {
            next = next.deriveChild(index).getOrThrow()
        }

        return Result.success(next)
    }

    private fun deriveChild(index: Int): Result<ExtendedSecretKey> {
        val mac: Mac = HMac(SHA512Digest())
        val keyParameter = KeyParameter(this.chainCode)
        mac.init(keyParameter)
        mac.update(0.toByte())
        mac.update(this.secretKey, 0, this.secretKey.size)

        // Solana uses hardened derivation
        val hardenedIndex = (1u shl 31) or index.toUInt()

        val indexBEBytes = hardenedIndex.toBEByteArray()
        mac.update(indexBEBytes, 0, indexBEBytes.size)

        val output = ByteArray(mac.macSize)
        mac.doFinal(output, 0)

        val secretKey = output.sliceArray(0..31)
        val chainCode = output.sliceArray(32..63)

        return Result.success(
            ExtendedSecretKey(
                depth = this.depth + 1,
                childIndex = index,
                secretKey = secretKey,
                chainCode = chainCode
            )
        )
    }

    fun keyPair(): SolanaKeyPair {
        val privateKey = Ed25519PrivateKeyParameters(secretKey)
        val publicKey = privateKey.generatePublicKey()
        return SolanaKeyPair(
            publicKey.encoded,
            privateKey.encoded
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExtendedSecretKey

        if (!secretKey.contentEquals(other.secretKey)) return false
        if (!chainCode.contentEquals(other.chainCode)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = secretKey.contentHashCode()
        result = 31 * result + chainCode.contentHashCode()
        return result
    }
}
