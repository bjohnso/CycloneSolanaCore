package com.cyclone.solana.core.constants

enum class SolanaNetwork(val defaultRpcUrl: String) {
    MAINNET("https://api.mainnet-beta.solana.com"),
    TESTNET("https://api.testnet.solana.com"),
    DEVNET("https://api.devnet.solana.com")
}