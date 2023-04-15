package com.cyclone.solana.core.constants

class RPC {
    object RPCMethods {
        const val GET_LATEST_BLOCKHASH = "getLatestBlockhash"
        const val GET_BALANCE = "getBalance"
        const val SEND_TRANSACTION = "sendTransaction"
        const val GET_TRANSACTION = "getTransaction"
        const val GET_TOKEN_ACCOUNTS_BY_OWNER = "getTokenAccountsByOwner"
        const val GET_ACCOUNT_INFO = "getAccountInfo"
    }

    object RPCMethodIds {
        const val GET_LATEST_BLOCKHASH = 1
        const val GET_BALANCE = 2
        const val SEND_TRANSACTION = 3
        const val GET_TRANSACTION = 4
        const val GET_TOKEN_ACCOUNTS_BY_OWNER = 5
        const val GET_ACCOUNT_INFO = 6
    }

    object RPCParams {
        const val COMMITMENT = "commitment"
    }
}