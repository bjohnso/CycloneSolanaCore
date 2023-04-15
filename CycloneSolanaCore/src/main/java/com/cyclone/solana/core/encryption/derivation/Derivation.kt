package com.cyclone.solana.core.encryption.derivation

import com.cyclone.solana.core.encryption.ExtendedSecretKey
import com.cyclone.solana.core.encryption.Utils
import com.cyclone.solana.core.extensions.base58ToByteArray
import com.cyclone.solana.core.extensions.binaryToHex
import com.cyclone.solana.core.extensions.toBinaryString
import net.i2p.crypto.eddsa.math.GroupElement
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable
import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.bouncycastle.crypto.params.KeyParameter
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import org.bouncycastle.math.ec.rfc8032.Ed25519
import java.util.Arrays

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

    fun findProgramAddress(seeds: List<ByteArray>, programId: String): Pair<ByteArray, Int> {
        var nonce = 255
        var address: ByteArray

        while (nonce != 0) {
            try {
                val seedsWithNonce = seeds.toMutableList().apply { add(byteArrayOf(nonce.toByte())) }
                address = createProgramAddress(seedsWithNonce, programId)
            } catch (e: Exception) {
                nonce--
                continue
            }
            return Pair(address, nonce)
        }
        throw IllegalStateException("Unable to find a viable program address nonce")
    }


    fun createProgramAddress(seeds: List<ByteArray>, programId: String): ByteArray {
        val programIdBytes = programId.base58ToByteArray()
        val programDerivedAddressLiteral = "ProgramDerivedAddress".toByteArray()

        val seedsSize = seeds.reduce { acc, bytes -> acc + bytes }.size

        val maxSeedLength = 32
        val buffer = ByteBuffer.allocate(
            seedsSize + programIdBytes.size + programDerivedAddressLiteral.size
        )

        for (seed in seeds) {
            if (seed.size > maxSeedLength) {
                throw IllegalArgumentException("Max seed length exceeded")
            }
            buffer.put(seed)
        }

        buffer.put(programIdBytes)
        buffer.put("ProgramDerivedAddress".toByteArray())

        val sha256 = MessageDigest.getInstance("SHA-256")
        val publicKeyBytes = sha256.digest(buffer.array())

        fun isOnCurve(publicKeyBytes: ByteArray): Boolean {
            return try {
                val curve = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519)
                val group = curve.curve
                val a = GroupElement(group, publicKeyBytes)
                a.isOnCurve
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        if (isOnCurve(publicKeyBytes)) {
            throw IllegalStateException("Invalid seeds, address must fall off the curve")
        }

        return publicKeyBytes
    }
}