package com.anatnaso.nasocoin.server.database.wallet

import com.anatnaso.nasocoin.server.database.Database
import com.anatnaso.nasocoin.server.database.account.UserAccount
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.server.database.exception.HandleConsumedException
import org.apache.commons.codec.digest.DigestUtils
import java.math.BigInteger
import kotlin.jvm.Throws

class WalletHandle (
    private val wallet: Wallet,
    private val account: UserAccount,
    private val access: Database.DatabaseDirectAccess
) {
    private var isConsumed = false

    fun isConsumed(): Boolean = isConsumed

    @Throws(HandleConsumedException::class)
    fun deleteWallet() {
        if (isConsumed) throw HandleConsumedException(
            "Could not delete wallet '${wallet.publicToken}' (pub): Handle already consumed"
        )

        synchronized(access) {
            access.walletsByPublicToken.remove(wallet.publicToken)
            access.walletsByPrivateToken.remove(wallet.privateToken)

            val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
                ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

            wallets.remove(wallet)

            val rootWallet = access.walletsByPublicToken[access.rootWalletPublicToken]
                ?: throw IllegalStateException("Missing root wallet")

            rootWallet.walletFunds.add(wallet.walletFunds)
        }

        isConsumed = true
    }

    @Throws(HandleConsumedException::class, NoSuchUserException::class, Exception::class)
    fun transferOwnershipByIdentifier(userIdentifier: String): WalletHandle {
        if (isConsumed) throw HandleConsumedException(
            "Could not transfer ownership of wallet '${wallet.publicToken}' (pub): Handle already consumed"
        )

        val newOwner = access.accounts.get(userIdentifier)
            ?: throw NoSuchUserException("Could not transfer ownership of wallet '${wallet.publicToken}' (pub): No such user '$userIdentifier'")

        val newOwnerWallets = access.walletsByOwnerIdentifier[newOwner.userIdentifier]
            ?: throw IllegalStateException("Missing wallet set for user ${newOwner.username}")

        val oldOwnerWallets = access.walletsByOwnerIdentifier[account.userIdentifier]
            ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

        synchronized(access) {
            try {
                oldOwnerWallets.remove(wallet)
                newOwnerWallets.add(wallet)

                wallet.ownerUserIdentifier = newOwner.userIdentifier

                isConsumed = true
            } catch (e: Exception) {
                // Rollback or reset
                isConsumed = false
                throw e
            }
        }

        return WalletHandle(wallet, newOwner, access)
    }

    @Throws(HandleConsumedException::class, NoSuchUserException::class, Exception::class)
    fun transferOwnershipByUsername(username: String): WalletHandle {
        try {
            return transferOwnershipByIdentifier(DigestUtils.sha256Hex(username))
        } catch (e: NoSuchUserException) {
            throw NoSuchUserException("Could not transfer ownership of wallet '${wallet.publicToken}' (pub): No such user '$username'", e)
        }
    }

    fun getPublicToken(): String = wallet.publicToken
    fun getPrivateToken(): String = wallet.privateToken
    fun getOwnerIdentifier(): String = wallet.ownerUserIdentifier
    fun getWalletFunds(): BigInteger = wallet.walletFunds
}