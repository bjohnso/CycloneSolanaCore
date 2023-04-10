package com.cyclone.solana.core.datamodel.dto.solanaRPC.result

import TransactionResult
import androidx.annotation.Keep
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.account_info.AccountInfo
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.blockhash.Blockhash
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account.TokenAccount
import com.cyclone.solana.core.datamodel.serializer.solanaRPC.result.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Keep
open class Result(
    open val context: Context? = null,
    open val value: JsonElement? = null
) {
    @Serializable(with = GetBalanceResultSerializer::class)
    data class GetBalanceResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val getBalanceResult: Long
    ): Result(context, value)

    @Serializable(with = GetLatestBlockhashResultSerializer::class)
    data class GetLatestBlockhashResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val getLatestBlockhashResult: Blockhash
    ): Result(context, value)

    @Serializable(with = SendTransactionResultSerializer::class)
    data class SendTransactionResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val sendTransactionResult: String
    ): Result(context, value)

    @Serializable(with = GetTransactionResultSerializer::class)
    data class GetTransactionResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val getTransactionResult: TransactionResult
    ): Result(context, value)

    @Serializable(with = GetTokenAccountsByOwnerResultSerializer::class)
    data class GetTokenAccountsByOwnerResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val getTokenAccountsByOwnerResult: List<TokenAccount>
    ): Result(context, value)

    @Serializable(with = GetAccountInfoResultSerializer::class)
    data class GetAccountInfoResult(
        override val context: Context? = null,
        override val value: JsonElement? = null,
        val getAccountInfoResult: AccountInfo
    ): Result(context, value)
}
