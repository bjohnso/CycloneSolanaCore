package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.Ed25119Signer
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class SigningUnitTest {
    @Test
    fun is_signature_correct() {
        val expected = Base58Decoder.invoke(
            "2xcrbV9CyfkKav4cyUTvhbDETR1ZQZDh3mHj1Fd5tuc2FBk53xgeCkKfM1qKJBCmSj3S4e7eMHurB3JWGWgViMgN"
        )

        val publicKeyParameters = Ed25519PublicKeyParameters(
            Base58Decoder.invoke("DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
        )

        val privateKeyParameters = Ed25519PrivateKeyParameters(
            Base58Decoder.invoke("7oLVLF7Pwxdwc6BUqLGdTgg6y5hMcgW3gkwwRQsiRrRT")
        )

        val keyPair = AsymmetricCipherKeyPair(
            publicKeyParameters,
            privateKeyParameters
        )

        val inputMessage = Base58Decoder.invoke(
            "87PYuLJkbJfgFnkDWHsaURDReR7xSajZCnNdUEXtPTPmsxGFzLqN9NvGWVK8QxgCEsgp7QtMiAHGgHfVP5h4K4uhpUyZwBoZaV12jAunHwT3Wi9MBG9kUE1NHEQJceUCTvPNsJqAGi6emQ3KPKgNwYQ4kP3ob9YRDjQF83nGmKW9KdZzKP3H36nyY4DYYFD4tXenZA6iHP7V"
        )

        val signatureResult = Ed25119Signer.invoke(
            inputMessage,
            keyPair
        )

        assertArrayEquals(
            expected,
            signatureResult
        )
    }
}