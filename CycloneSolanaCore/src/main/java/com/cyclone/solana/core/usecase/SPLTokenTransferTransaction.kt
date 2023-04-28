package com.cyclone.solana.core.usecase

import com.cyclone.solana.core.constants.Address
import com.cyclone.solana.core.datamodel.dto.crypto.*

object SPLTokenTransferTransaction {
    operator fun invoke(
        sourceAddress: String, // SPL token account address
        destinationAddress: String, // SPL token account address
        ownerAddress: String, // SPL token owner address
        blockhash: String,
        tokenAmount: Long
    ): Transaction {
        val header = Header(
            noSigs = 1,
            noSignedReadOnlyAccounts = 0,
            noUnsignedReadOnlyAccounts = 1
        )

        val instruction = TransferInstruction(
            programIdIndex = 3,
            accountIndices = listOf(1, 2, 0),
            data = SPLTokenTransferInstructionData(amount = tokenAmount)
        )

        val message = Message(
            header = header,
            accountAddresses = listOf(
                ownerAddress,
                sourceAddress,
                destinationAddress,
                Address.ProgramAddresses.TOKEN_PROGRAM,
            ),
            blockhash = blockhash,
            instructions = listOf(instruction)
        )

        return Transaction(
            message = message
        )
    }
}