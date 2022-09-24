package com.cyclone.solana.core.datamodel

data class Message(
    val header: Header,
    val accountAddresses: List<String>,
    val blockhash: String,
    val instructions: List<Instruction>
)
