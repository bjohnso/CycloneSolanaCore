package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.usecase.*
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.junit.Assert
import org.junit.Test

class SPLTokenTransferTransactionTest {
    @Test
    fun spl_token_transfer_transaction_is_correct() {
        val expectedUnsigned = Base58Decoder.invoke(
            "7Higry93SkNgKpDUejjxT6VADsAvS6iMrDaCB1zVErQJVj11sqzCVP7wT6a4qeL7uxFcGc5KCjjqMQZsgTCsGZ7YFizvVsUMzVn2ugaXw1ptWxzmKe4qsDptGtfCzjS7NpfUeHUo99dm3T1vj9wkWkXmMToZcSdkofJTiBf72H6DtGA7VqZAHpi9QMr8QFWrRWDZQfaaJsd3d3kJ6HLkQ21jdvupJ71JZhX2rmTA92myLFegPHHrf"
        )

        val fromOwnerAddress = "RhDUBhjrfTjaaetKT35MMovJgbemioo7hadd7utpaWr"
        val fromSPLAddress = "F2av2u8pB49JWYXvytx7CnNZGpypobcaaD6srDip5JWQ"
        val toSPLAddress = "4Y17ZEEqDHBU3Z7J7coBMpR43kmztdFkWL1LfyVUvXBE"
        val blockhash = "CztHNnNatexftES7vq4Z8nG7fXsZfQKM6KQ5HQkLUdL4"
        val tokenAmount = Unit.Units.UNITS_PER_USDC

        val result = SPLTokenTransferTransaction.invoke(
            sourceAddress = fromSPLAddress,
            destinationAddress = toSPLAddress,
            ownerAddress = fromOwnerAddress,
            blockhash = blockhash,
            tokenAmount = tokenAmount
        )

        Assert.assertArrayEquals(
            expectedUnsigned,
            result.serialise()
        )
    }

    @Test
    fun signed_spl_token_transfer_transaction_correct() {
        val expectedSigned = Base58Decoder.invoke(
            "5YxNy2bdSi7R9gjegR2mHuWPMhReqfb74Zyrq4qkqQ6MmEu1L5kWkTDu4BUKmmTiMqLd2Aa3Mn1eRWEogYdA8LDubcpqitY6XHyZjqjph5Mc7wkc3Kdg4YyNwy1uqwRcDNYami5Lyi4e9M7WawsrVhMyjJBr3Qo6ii98fM1wteKgfTXXyGt6z8XDP1jxWvXNQESK2a619rRwfR2cBePekEVMaxnFYXeVBbqfhzz5GP4Kwb2n4jftuuhKBga3LEV8ay4UMTAgM4tzrUcDpYoDdJNpvADESoCMobRty1p2vubTs6ta1xFDDfWf4NQe5DTfiKohL3dMdCdWK9"
        )

        val publicKeyParameters = Ed25519PublicKeyParameters(
            Base58Decoder.invoke("RhDUBhjrfTjaaetKT35MMovJgbemioo7hadd7utpaWr")
        )

        val privateKeyParameters = Ed25519PrivateKeyParameters(
            Base58Decoder.invoke("44t3ZkstYEjazJNh4hRn3NhdEZWuth3cHoqogbq8Xdh6")
        )

        val keyPair = AsymmetricCipherKeyPair(
            publicKeyParameters,
            privateKeyParameters
        )

        val fromOwnerAddress = "RhDUBhjrfTjaaetKT35MMovJgbemioo7hadd7utpaWr"
        val fromSPLAddress = "F2av2u8pB49JWYXvytx7CnNZGpypobcaaD6srDip5JWQ"
        val toSPLAddress = "4Y17ZEEqDHBU3Z7J7coBMpR43kmztdFkWL1LfyVUvXBE"
        val blockhash = "CztHNnNatexftES7vq4Z8nG7fXsZfQKM6KQ5HQkLUdL4"
        val tokenAmount = Unit.Units.UNITS_PER_USDC

        val result = SPLTokenTransferTransaction.invoke(
            sourceAddress = fromSPLAddress,
            destinationAddress = toSPLAddress,
            ownerAddress = fromOwnerAddress,
            blockhash = blockhash,
            tokenAmount = tokenAmount
        ).apply {
            sign(
                listOf(keyPair)
            )
        }

        Assert.assertArrayEquals(
            expectedSigned,
            result.serialise()
        )
    }
}