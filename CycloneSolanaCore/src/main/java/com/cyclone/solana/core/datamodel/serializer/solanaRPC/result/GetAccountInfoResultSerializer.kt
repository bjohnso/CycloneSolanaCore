package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Context
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.account_info.AccountInfo
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.*

object GetAccountInfoResultSerializer: KSerializer<Result.GetAccountInfoResult> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result.GetAccountInfoResult") {
        element("value", JsonElement.serializer().descriptor)
        element("context", Context.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Result.GetAccountInfoResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
        val context: JsonElement? = result["context"]
        val value: JsonObject = result["value"]!!.jsonObject

        return Result.GetAccountInfoResult(
            value = value,
            getAccountInfoResult = json.decodeFromJsonElement(AccountInfo.serializer(), value),
            context = context?.let { json.decodeFromJsonElement(Context.serializer(), it) }
        )
    }

    override fun serialize(encoder: Encoder, value: Result.GetAccountInfoResult) {
        encoder.encodeSerializableValue(Result.GetAccountInfoResult.serializer(), value)
    }
}