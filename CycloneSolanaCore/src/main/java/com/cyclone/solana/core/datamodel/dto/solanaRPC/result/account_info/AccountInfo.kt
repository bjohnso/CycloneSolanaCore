package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.account_info

import kotlinx.serialization.Serializable

@Serializable
data class AccountInfo(
    val executable: Boolean? = null,
    val owner: String? = null,
    val lamports: Long? = null,
    val data: List<String>? = null,
    val rentEpoch: Long? = null,
    val space: Long? = null
)
