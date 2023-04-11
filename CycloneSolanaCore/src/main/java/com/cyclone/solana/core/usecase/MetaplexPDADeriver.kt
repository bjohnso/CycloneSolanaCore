package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Address
import com.cyclone.solana.core.encryption.derivation.Derivation
import com.cyclone.solana.core.extensions.base58ToByteArray

object MetaplexPDADeriver {
    operator fun invoke(mintAddress: String): Pair<ByteArray, Int> {
        val seeds = listOf(
            "metadata".toByteArray(),
            Address.ProgramAddresses.TOKEN_META_DATA_PROGRAM.base58ToByteArray(),
            mintAddress.base58ToByteArray()
        )

        return Derivation.findProgramAddress(
            seeds,
            Address.ProgramAddresses.TOKEN_META_DATA_PROGRAM
        )
    }
}