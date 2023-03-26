package com.cyclone.solana.core.datamodel.dto.keypair

import com.cyclone.solana.core.datamodel.dto.derivation.DerivationPath
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.params.HKDFParameters
import org.bouncycastle.math.ec.rfc8032.Ed25519
import java.nio.ByteBuffer

data class SolanaKeypair(
    val publicKey: ByteArray,
    val privateKey: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SolanaKeypair

        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!privateKey.contentEquals(other.privateKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = publicKey.contentHashCode()
        result = 31 * result + privateKey.contentHashCode()
        return result
    }
}

fun deriveKeypair(seed: ByteArray, path: DerivationPath): SolanaKeypair {
    val privateKey = derivePrivateKey(seed, path)
    val publicKey = ByteArray(32)
    Ed25519.generatePublicKey(privateKey, 0, publicKey, 0)
    return SolanaKeypair(privateKey, publicKey)
}

fun derivePrivateKey(seed: ByteArray, path: DerivationPath): ByteArray {
    // Define derivation path as bytes
    val pathData = ByteBuffer.allocate(20).apply {
        putInt(path.purpose or 0x80000000.toInt())
        putInt(path.coinType or 0x80000000.toInt())
        path.account?.let {
            println("account")
            this.putInt(path.account or 0x80000000.toInt())
        }
        path.change?.let {
            println("change")
            this.putInt(path.change)
        }
        path.addressIndex?.let {
            println("addressIndex")
            this.putInt(path.addressIndex)
        }
    }.array()

    // Generate the master key using HKDF
    val hkdf = HKDFBytesGenerator(SHA256Digest())
    hkdf.init(HKDFParameters(seed, "ed25519 seed".toByteArray(), null))
    val masterKey = ByteArray(64)
    hkdf.generateBytes(masterKey, 0, 64)

    // Iterate through the derivation path and derive the private key
    var currentKey = masterKey
    for (i in 0 until 5) {
        val data = ByteBuffer.allocate(36).apply {
            put(currentKey.copyOfRange(0, 32))
            put(pathData.copyOfRange(4 * i, 4 * (i + 1)))
        }.array()
        val derivedKey = ByteArray(64)
        hkdf.init(HKDFParameters(currentKey.copyOfRange(32, 64), data, null))
        hkdf.generateBytes(derivedKey, 0, 64)
        currentKey = derivedKey
    }

    return currentKey.copyOfRange(0, 32)
}