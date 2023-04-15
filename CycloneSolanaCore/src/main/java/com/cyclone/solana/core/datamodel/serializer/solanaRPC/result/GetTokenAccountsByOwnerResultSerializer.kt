package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Context
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.token_account.TokenAccount
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object GetTokenAccountsByOwnerResultSerializer: KSerializer<Result.GetTokenAccountsByOwnerResult> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result.GetTokenAccountsByOwnerResult") {
        element("value", JsonElement.serializer().descriptor)
        element("context", Context.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Result.GetTokenAccountsByOwnerResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
        val context: JsonElement? = result["context"]
        val value: JsonArray = result["value"]!!.jsonArray

        return Result.GetTokenAccountsByOwnerResult(
            value = value,
            getTokenAccountsByOwnerResult = value.map { json.decodeFromJsonElement(TokenAccount.serializer(), it) },
            context = context?.let { json.decodeFromJsonElement(Context.serializer(), it) }
        )
    }

    override fun serialize(encoder: Encoder, value: Result.GetTokenAccountsByOwnerResult) {
        encoder.encodeSerializableValue(Result.GetTokenAccountsByOwnerResult.serializer(), value)
    }
}