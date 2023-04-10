package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Context
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object GetBalanceResultSerializer: KSerializer<Result.GetBalanceResult> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result.GetBalanceResult") {
        element("value", JsonElement.serializer().descriptor)
        element("context", Context.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Result.GetBalanceResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
        val context: JsonElement? = result["context"]
        val value: JsonPrimitive = result["value"]!!.jsonPrimitive

        return Result.GetBalanceResult(
            value = value,
            getBalanceResult = value.long,
            context = context?.let { json.decodeFromJsonElement(Context.serializer(), it) }
        )
    }

    override fun serialize(encoder: Encoder, value: Result.GetBalanceResult) {
        encoder.encodeSerializableValue(Result.GetBalanceResult.serializer(), value)
    }
}