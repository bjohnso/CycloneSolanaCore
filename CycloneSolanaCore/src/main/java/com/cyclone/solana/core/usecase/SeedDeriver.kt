package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.datamodel.dto.derivation.DerivationPath
import com.cyclone.solana.core.datamodel.dto.keypair.SolanaKeypair
import com.cyclone.solana.core.encryption.derivation.Derivation

object SeedDeriver {
    /**
     * Derives Ed25519 Seed using derivation path m/44'/501'/X'
     */
    operator fun invoke(mnemonic: List<String>): List<SolanaKeypair> {
        val seed = Derivation.generateDerivationSeed(mnemonic)
        val masterExtendedSecretKey = Derivation.generateMasterExtendSecretKey(seed)

        return List(64) {
            val derivationPath = DerivationPath(
                account = it
            )

            masterExtendedSecretKey
                .derive(derivationPath.toList())
                .getOrThrow()
                .keyPair()
        }
    }
}