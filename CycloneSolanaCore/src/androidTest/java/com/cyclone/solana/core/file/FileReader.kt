package com.cyclone.solana.core.file

import java.io.InputStreamReader

class FileReader {
    object FileResource {
        const val getLatestBlockHash = "solanaRPC/get_latest_blockhash.json"
        const val getLatestBlockHashError = "solanaRPC/get_latest_blockhash_error.json"
        const val getBalance = "solanaRPC/get_balance.json"
        const val getBalanceError = "solanaRPC/get_balance_error.json"
        const val sendTransaction = "solanaRPC/send_transaction.json"
        const val sendTransactionError = "solanaRPC/send_transaction_error.json"
    }

    fun readJsonFile(fileName: String): String {
        return InputStreamReader(
            this.javaClass.classLoader!!.getResourceAsStream(fileName)
        ).use { it.readText() }
    }

    companion object {
        val instance = FileReader()
    }
}