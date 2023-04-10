package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.blockhash.Blockhash
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Context
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object GetLatestBlockhashResultSerializer: KSerializer<Result.GetLatestBlockhashResult> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result.GetLatestBlockhashResult") {
        element("value", JsonElement.serializer().descriptor)
        element("context", Context.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Result.GetLatestBlockhashResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject
        val context: JsonElement? = result["context"]
        val value: JsonElement = result["value"]!!

        return Result.GetLatestBlockhashResult(
            value = value,
            getLatestBlockhashResult = json.decodeFromJsonElement(Blockhash.serializer(), value),
            context = context?.let { json.decodeFromJsonElement(Context.serializer(), it) }
        )
    }

    override fun serialize(encoder: Encoder, value: Result.GetLatestBlockhashResult) {
        encoder.encodeSerializableValue(Result.GetLatestBlockhashResult.serializer(), value)
    }
}