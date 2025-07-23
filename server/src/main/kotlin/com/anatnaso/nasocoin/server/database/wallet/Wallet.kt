package com.anatnaso.nasocoin.server.database.wallet

import java.io.Serializable
import java.math.BigInteger
import java.util.*

@ConsistentCopyVisibility
data class Wallet private constructor (
    val publicToken: String,
    val privateToken: String,
    var ownerUserIdentifier: String,
    val walletFunds: BigInteger,
) : Serializable {
    companion object {
        private val serialVersionUID: Long = 1L
    }

    constructor(ownerUserIdentifier: String) : this (
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        ownerUserIdentifier,
        BigInteger("0"),
    )
}
