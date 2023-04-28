package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SolTransferTransaction
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.junit.Assert
import org.junit.Test

class SolTransferTransactionUnitTest {
    @Test
    fun sol_transfer_transaction_is_correct() {
        val expectedUnsigned = Base58Decoder.invoke(
            "87PYuLJkbJfgFnkDWHsaURDReR7xSajZCnNdUEXtPTPmsxGFzLqN9NvGWVK8QxgCEsgp7QtMiAHGgHfVP5h4K4uhpUyZwBoZaV12jAunHwT3Wi9MBG9kUE1NHEQJceUCTvPNsJqAGi6emQ3KPKgNwYQ4kP3ob9YRDjQF83nGmKW9KdZzKP3H36nyY4DYYFD4tXenZA6iHP7V"
        )

        val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
        val toAddress = "W5RJACTF8t4TC1LSBr4SXqkuZkFMM8yZFvRqqfM6tqM"
        val blockhash = "E1fZFLAd1aB8xUG6mx4KuEfG4kNKXBY99W26o5RPbbvk"
        val lamports = Unit.Units.LAMPORTS_PER_SOL

        val result = SolTransferTransaction.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        )

        Assert.assertArrayEquals(
            expectedUnsigned,
            result.serialise()
        )
    }

    @Test
    fun signed_sol_transfer_transaction_correct() {
        val expectedSigned = Base58Decoder.invoke(
            "5N4f7CayFbX4XNZK9C4dZpmAGZiuMNtWELJHhwGn1ttZh6oWWYHsWdNgfx4LWHQhMtnEmXvAvTucmVaGKvaHxQFNQNoaJev99gNSACu6ukMbHktHHAQ4EWMgYvxdnjDkjR4Hrh97Auhbx1npCQAjFmBvsaLUgxwcmjd7uzftnm5P8CctSmyU22SBvAe2CuzZAC447ZpqDxhBq5gUumAxsmLRsW9PABznD88zquP2y5Kka1oPnvhdoBfFB9n9eP7T4uUgsbYS4E2KphUgCDAr9WMqXHMa8w9gsNrL7"
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

        val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
        val toAddress = "W5RJACTF8t4TC1LSBr4SXqkuZkFMM8yZFvRqqfM6tqM"
        val blockhash = "7jzpE4VGyhXViUcA77UmfK8r4ApfCK3J4mtrJKGF7EiE"
        val lamports = Unit.Units.LAMPORTS_PER_SOL

        val result = SolTransferTransaction.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        ).apply {
            sign(listOf(keyPair))
        }

        println(
            Base58Encoder(
                result.serialise()
            )
        )

        Assert.assertArrayEquals(
            expectedSigned,
            result.serialise()
        )
    }
}