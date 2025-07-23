package com.anatnaso.nasocoin.server.database.wallet

import java.io.Serializable
import java.math.BigInteger
import java.util.*

@ConsistentCopyVisibility
data class Wallet private constructor (
    val publicToken: String,
    val privateToken: String,
    val walletFunds: BigInteger
) : Serializable {
    companion object {
        private val serialVersionUID: Long = 1L
    }

    constructor() : this (
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        BigInteger("0"),
    )
}
