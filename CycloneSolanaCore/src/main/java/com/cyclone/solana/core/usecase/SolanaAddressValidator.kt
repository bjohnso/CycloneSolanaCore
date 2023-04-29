package com.cyclone.solana.core.usecase

object SolanaAddressValidator {
    operator fun invoke(address: String): Boolean {
        try {
            val bytes = Base58Decoder(address)

            if (bytes.size != 32) {
                return false
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }
}