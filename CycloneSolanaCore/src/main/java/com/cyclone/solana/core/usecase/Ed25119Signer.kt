package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Signing
import com.cyclone.solana.core.extensions.toEd25519PrivateKeyKeyParameters
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.signers.Ed25519Signer

object Ed25119Signer {
    operator fun invoke(
        message: ByteArray,
        keyPair: AsymmetricCipherKeyPair
    ): ByteArray {
        val signer = Ed25519Signer()
        val privateKey = keyPair.private.toEd25519PrivateKeyKeyParameters

        signer.init(true, privateKey)
        signer.update(message, 0, message.size)
        val signature = signer.generateSignature()

        assert(signature.size == Signing.LENGTHS.SIGNATURE_LENGTH_BYTES) {
            "Signature length must be 64 bytes"
        }

        return signature
    }
}