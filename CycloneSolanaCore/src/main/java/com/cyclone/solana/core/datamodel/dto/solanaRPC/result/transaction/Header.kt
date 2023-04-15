package com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction

import kotlinx.serialization.Serializable

@Serializable
data class Header(
    val numReadonlySignedAccounts: Int? = null,
    val numReadonlyUnsignedAccounts: Int? = null,
    val numRequiredSignatures: Int? = null,
)
