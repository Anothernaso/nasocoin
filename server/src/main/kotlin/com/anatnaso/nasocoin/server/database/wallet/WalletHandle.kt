package com.anatnaso.nasocoin.server.database.wallet

import com.anatnaso.nasocoin.server.database.Database
import com.anatnaso.nasocoin.server.database.account.UserAccount
import java.io.Serializable

class WalletHandle (
    private val wallet: Wallet,
    private val ownerAccount: UserAccount,
    private val access: Database.DatabaseDirectAccess
) {
    private var isConsumed = false

    fun isConsumed(): Boolean = isConsumed
}