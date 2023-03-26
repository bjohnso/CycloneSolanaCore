package com.cyclone.solana.core.datamodel.dto.derivation

data class DerivationPath(
    val account: Int? = null,
    val change: Int? = null,
    val addressIndex: Int? = null
) {
    val purpose: Int = 44
    val coinType: Int = 501

    companion object {
        fun fromPath(path: String): DerivationPath {
            val pathParts = path
                .replace("'", "")
                .split("/")
                .mapNotNull { it.toIntOrNull() }

            assert(pathParts.size in 0..3) {
                "invalid derivation path: $path"
            }

            return DerivationPath(
                account = pathParts.getOrNull(0),
                change = pathParts.getOrNull(1),
                addressIndex = pathParts.getOrNull(2)
            )
        }
    }

    fun toList() = listOfNotNull(
        purpose,
        coinType,
        account,
        change,
        addressIndex
    )
}

