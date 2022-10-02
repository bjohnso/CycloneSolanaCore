package com.cyclone.solana.core.constants

class RPC {
    object RPCMethods {
        const val GET_LATEST_BLOCKHASH = "getLatestBlockhash"
        const val GET_BALANCE = "getBalance"
        const val SEND_TRANSACTION = "sendTransaction"
        const val GET_TRANSACTION = "getTransaction"
    }

    object RPCParams {
        const val COMMITMENT = "commitment"
    }
}