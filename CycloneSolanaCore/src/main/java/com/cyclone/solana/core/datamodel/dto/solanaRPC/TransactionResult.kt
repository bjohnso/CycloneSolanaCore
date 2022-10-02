package com.cyclone.solana.core.datamodel.dto.solanaRPC

data class TransactionResult(
    val blockTime: Long,
    val meta: Meta,
    val slot: Long,
) {
    data class Meta(
        val err: Any?, // TODO: Create Pojo for err
        val fee: Long,
        val logMessages: List<String>,
        val postBalances: List<Long>,
    )
}