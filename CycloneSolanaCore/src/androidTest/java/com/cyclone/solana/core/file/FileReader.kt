package com.cyclone.solana.core.file

import java.io.InputStreamReader

object FileReader {
    fun readJsonFile(fileName: String): String {
        val reader = InputStreamReader(
            this.javaClass.classLoader?.getResourceAsStream(fileName)
        )

        val content = reader.readText()

        reader.close()

        return content
    }
}