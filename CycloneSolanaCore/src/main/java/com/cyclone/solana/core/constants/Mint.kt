package com.cyclone.solana.core.constants

class Mint(val network: SolanaNetwork) {
    companion object {
        private const val USDC_MAINNET = "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v"
        private const val USDC_TESTNET = "CpMah17kQEL2wqyMKt3mZBdTnZbkbfx4nqmQMFDP5vwp"
        private const val USDC_DEVNET = "4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU"
    }

    fun getUSDCMintAddress(): String {
        return when (network) {
            SolanaNetwork.MAINNET -> USDC_MAINNET
            SolanaNetwork.TESTNET -> USDC_TESTNET
            SolanaNetwork.DEVNET -> USDC_DEVNET
        }
    }
}