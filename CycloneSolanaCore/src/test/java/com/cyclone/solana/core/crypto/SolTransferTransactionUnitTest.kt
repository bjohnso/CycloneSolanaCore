package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.usecase.Base58Decoder
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
            "4q6hftAxkbmYue47iaGbyAepZwSXxWut83hwnMkQjgFYCb6n6z58rNKt4V4Gv6cLw5c5qQR9j3DH5xYgWYpbfVZEA6CpvP6zm8FeanzQaVURU2wtgpoxbArnzSf1h6ZU5yXx1pt566eud5RE5SnYoxmMQGbPVq4J4pyec851vNRAukrtgrPAohjQhz7i4vEaUfVDKNi5cTM9TxXM7atRGxBJcxK5Txugh2Zi5eSR7UqUAMHb9FPqjroiDVEhkQMDKsBu7ZxdyyH8qhtnToz3SkJfThPCsHh5fb9y1"
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
        val blockhash = "E1fZFLAd1aB8xUG6mx4KuEfG4kNKXBY99W26o5RPbbvk"
        val lamports = Unit.Units.LAMPORTS_PER_SOL

        val result = SolTransferTransaction.invoke(
            fromAddress,
            toAddress,
            blockhash,
            lamports
        ).apply {
            sign(listOf(keyPair))
        }

        Assert.assertArrayEquals(
            expectedSigned,
            result.serialise()
        )
    }
}