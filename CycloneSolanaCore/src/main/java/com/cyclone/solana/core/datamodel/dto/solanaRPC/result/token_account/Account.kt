package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val data: AccountData? = null,
    val executable: Boolean? = null,
    val lamports: Long? = null,
    val owner: String? = null,
    val rentEpoch: Long? = null,
)
