package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Address
import com.cyclone.solana.core.datamodel.Header
import com.cyclone.solana.core.datamodel.Message
import com.cyclone.solana.core.datamodel.Transaction
import com.cyclone.solana.core.datamodel.TransferInstruction
import java.math.BigInteger

object SolTransfer {
    operator fun invoke(
        fromAddress: String,
        toAddress: String,
        blockhash: String,
        lamports: Int
    ): Transaction {
        val header = Header(
            noSigs = 1,
            noSignedReadOnlyAccounts = 1,
            noUnsignedReadOnlyAccounts = 0
        )

        val instruction = TransferInstruction(
            programIdIndex = 2,
            accountIndices = listOf(1, 2),
            data = BigInteger.valueOf(lamports.toLong()).toByteArray()
        )

        val message = Message(
            header = header,
            accountAddresses = listOf(
                Address.SYSTEM_PROGRAM,
                fromAddress,
                toAddress
            ),
            blockhash = blockhash,
            instructions = listOf(instruction)
        )

        return Transaction(
            signatures = listOf(),
            message = message
        )
    }
}