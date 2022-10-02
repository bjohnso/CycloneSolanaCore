package com.cyclone.solana.core.file

import java.io.InputStreamReader

class FileReader {
    object FileResource {
        const val getLatestBlockHash = "solanaRPC/get_latest_blockhash.json"
        const val getLatestBlockHashError = "solanaRPC/get_latest_blockhash_error.json"
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