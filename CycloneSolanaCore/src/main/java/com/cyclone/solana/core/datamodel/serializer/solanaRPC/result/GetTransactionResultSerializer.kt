package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import TransactionResult
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object GetTransactionResultSerializer: KSerializer<Result.GetTransactionResult> {
    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        prettyPrint = true
    }

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Result.GetTransactionResult") {}

    override fun deserialize(decoder: Decoder): Result.GetTransactionResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonObject

        return Result.GetTransactionResult(
            value = result,
            getTransactionResult = json.decodeFromJsonElement(TransactionResult.serializer(), result),
        )
    }

    override fun serialize(encoder: Encoder, value: Result.GetTransactionResult) {
        encoder.encodeSerializableValue(Result.GetTransactionResult.serializer(), value)
    }
}