package com.cyclone.solana.core.datamodel.serializer.solanaRPC.result

import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object SendTransactionResultSerializer: KSerializer<Result.SendTransactionResult> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Result.SendTransactionResult") {
        element("value", JsonElement.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Result.SendTransactionResult {
        val result = decoder.decodeSerializableValue(JsonElement.serializer()) as JsonPrimitive

        return Result.SendTransactionResult(
            value = result,
            sendTransactionResult = result.content,
        )
    }

    override fun serialize(encoder: Encoder, value: Result.SendTransactionResult) {
        encoder.encodeSerializableValue(Result.SendTransactionResult.serializer(), value)
    }
}