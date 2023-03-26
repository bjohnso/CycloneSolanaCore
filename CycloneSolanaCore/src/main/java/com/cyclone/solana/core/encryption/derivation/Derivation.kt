package com.cyclone.solana.core.encryption.derivation

import com.cyclone.solana.core.encryption.ExtendedSecretKey
import com.cyclone.solana.core.encryption.Utils
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter
import java.nio.charset.StandardCharsets

object Derivation {
    fun generateDerivationSeed(mnemonic: List<String>, passphrase: String = ""): ByteArray {
        val mnemonicStr = mnemonic.joinToString(" ")
        val salt = "mnemonic$passphrase"

        // Derive the seed using PBKDF2 with HMAC-SHA512
        val generator = PKCS5S2ParametersGenerator(SHA512Digest())
        generator.init(
            mnemonicStr.toByteArray(StandardCharsets.UTF_8),
            salt.toByteArray(StandardCharsets.UTF_8), 2048
        )

        return (generator.generateDerivedParameters(512) as KeyParameter).key
    }

    fun generateMasterExtendSecretKey(seed: ByteArray): ExtendedSecretKey {
        assert(seed.size == 64) {
            "Invalid seed length"
        }

        val masterKey = "ed25519 seed".toByteArray()
        val output = Utils.hmacSha512(masterKey, seed)

        val privateKey = output.sliceArray(0..31)
        val chainCode = output.sliceArray(32..63)

        return ExtendedSecretKey(
            privateKey,
            chainCode,
        )
    }
}