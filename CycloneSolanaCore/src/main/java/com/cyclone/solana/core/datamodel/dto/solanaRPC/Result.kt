package com.cyclone.solana.core.datamodel.dto.solanaRPC

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

@Keep
open class Result(
    open val context: Context? = null,
    @SerializedName("value")
    var rawValue: Any? = null
) {
    data class JsonResult(
        override val context: Context? = null,
        val value: JsonObject,
    ): Result(context) {
        companion object {
            fun fromResult(result: Result): JsonResult {
                return JsonResult(
                    context = result.context,
                    value = Gson().toJsonTree(result.rawValue as? LinkedTreeMap<*, *>).asJsonObject
                )
            }
        }
    }

    data class StringResult(
        override val context: Context? = null,
        val value: String,
    ): Result(context) {
        companion object {
            fun fromResult(result: Result): StringResult {
                return StringResult(
                    context = result.context,
                    value = result.rawValue as? String ?: ""
                )
            }
        }
    }

    data class LongResult(
        override val context: Context? = null,
        val value: Long,
    ): Result(context) {
        companion object {
            fun fromResult(result: Result): LongResult {
                return LongResult(
                    context = result.context,
                    value = (result.rawValue as? Double)?.toLong() ?: 0
                )
            }
        }
    }
}
