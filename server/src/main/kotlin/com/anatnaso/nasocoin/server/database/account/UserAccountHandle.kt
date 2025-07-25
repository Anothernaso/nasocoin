package com.anatnaso.nasocoin.server.database.account

import com.anatnaso.nasocoin.server.database.Database
import com.anatnaso.nasocoin.server.database.account.exception.UsernameOccupiedException
import com.anatnaso.nasocoin.server.database.exception.HandleConsumedException
import com.anatnaso.nasocoin.server.database.wallet.Wallet
import com.anatnaso.nasocoin.server.database.wallet.WalletHandle
import com.anatnaso.nasocoin.server.database.wallet.exception.NoSuchWalletException
import org.apache.commons.codec.digest.DigestUtils

class UserAccountHandle (
    private val account: UserAccount,
    private val access: Database.DatabaseDirectAccess,
) {
    private var isConsumed = false

    fun isConsumed(): Boolean = isConsumed

    @Throws(HandleConsumedException::class)
    fun deleteAccount() {
        if (isConsumed) throw HandleConsumedException (
            "Could not delete user account '${account.username}': Handle already consumed"
        )

        synchronized(access) {
            getWallets().forEach { wallet ->
                wallet.deleteWallet()
            }

            access.walletsByOwnerIdentifier.remove(account.userIdentifier)
            access.accounts.remove(account.userIdentifier)
        }

        isConsumed = true
    }

    @Throws(HandleConsumedException::class, UsernameOccupiedException::class)
    fun changeUsername(username: String) {
        if (isConsumed) throw HandleConsumedException (
            "Could not change username of user account '${account.username}': Handle already consumed"
        )

        if (
            access.accounts.containsKey(DigestUtils.sha256Hex(username))
            ) throw UsernameOccupiedException (
                "Could not change username of user account " +
                        "'${account.username}' to '$username': Username occupied"
            )

        synchronized(access) {
            access.accounts.remove(account.userIdentifier)

            val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
                ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

            access.walletsByOwnerIdentifier.remove(account.userIdentifier)

            account.username = username // This will automatically generate a new user identifier

            access.walletsByOwnerIdentifier[account.userIdentifier] = wallets
            access.accounts[account.userIdentifier] = account
        }

        val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]!!
        wallets.forEach { wallet ->
            wallet.ownerUserIdentifier = account.userIdentifier
        }
    }

    @Throws(HandleConsumedException::class)
    fun changeDisplayName(displayName: String) {
        if (isConsumed) throw HandleConsumedException (
            "Could not change display name of user account '${account.username}': Handle already consumed"
        )

        account.displayName = displayName
    }

    @Throws(HandleConsumedException::class)
    fun changePassword(password: String) {
        if (isConsumed) throw HandleConsumedException (
            "Could not change password of user account '${account.username}': Handle already consumed"
        )

        account.password = password
    }

    @Throws(HandleConsumedException::class)
    fun getDisplayName(): String {
        if (isConsumed) throw HandleConsumedException (
            "Could not get display name of user account '${account.username}': Handle already consumed"
        )

        return account.displayName
    }

    @Throws(HandleConsumedException::class)
    fun getUsername(): String {
        if (isConsumed) throw HandleConsumedException (
            "Could not get username of user account '${account.username}': Handle already consumed"
        )

        return account.username
    }

    @Throws(HandleConsumedException::class)
    fun getUserIdentifier(): String {
        if (isConsumed) throw HandleConsumedException (
            "Could not get user identifier of user account '${account.username}': Handle already consumed"
        )

        return account.userIdentifier
    }

    @Throws(HandleConsumedException::class)
    fun getPassword(): String {
        if (isConsumed) throw HandleConsumedException (
            "Could not get password of user account '${account.username}': Handle already consumed"
        )

        return account.password
    }

    @Throws(HandleConsumedException::class)
    fun createWallet(): WalletHandle {
        if (isConsumed) throw HandleConsumedException (
            "Could not create wallet for user account '${account.username}': Handle already consumed"
        )

        val wallet = Wallet(account.userIdentifier)

        synchronized(access) {
            access.walletsByPublicToken[wallet.publicToken] = wallet
            access.walletsByPrivateToken[wallet.privateToken] = wallet

            val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
                ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

            wallets.add(wallet)
        }

        return WalletHandle(wallet, account, access)
    }

    @Throws(HandleConsumedException::class, NoSuchWalletException::class)
    fun getWalletByPublicToken(publicToken: String): WalletHandle {
        return getWallet(publicToken, false)
    }

    @Throws(HandleConsumedException::class, NoSuchWalletException::class)
    fun getWalletByPrivateToken(privateToken: String): WalletHandle {
        return getWallet(privateToken, true)
    }

    @Throws(HandleConsumedException::class, NoSuchWalletException::class)
    private fun getWallet(token: String, isPrivate: Boolean): WalletHandle {
        if (isConsumed) throw HandleConsumedException (
            "Could not get wallet '$token' (${if (!isPrivate) "pub" else "prt"}) of user account '${account.username}': Handle already consumed"
        )

        val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
            ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

        var wallet: Wallet? = null
        wallets.forEach { it ->
            if (!isPrivate) {
                if (it.publicToken == token) wallet = it
            } else {
                if (it.privateToken == token) wallet = it
            }
        }

        wallet
            ?: throw NoSuchWalletException("Could not get wallet '$token' (${if (!isPrivate) "pub" else "prt"}) of user account '${account.username}': No such wallet")

        return WalletHandle(wallet, account, access)
    }

    @Throws(HandleConsumedException::class)
    fun getWallets(): MutableSet<WalletHandle> {
        if (isConsumed) throw HandleConsumedException (
            "Could not get wallets of user account '${account.username}': Handle already consumed"
        )

        val handles = mutableSetOf<WalletHandle>()

        val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
            ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

        wallets.forEach { wallet ->
            handles.add(WalletHandle(wallet, account, access))
        }

        return handles;
    }

    @Throws(HandleConsumedException::class)
    fun hasWalletByPublicToken(publicToken: String): Boolean {
        return hasWallet(publicToken, false)
    }

    @Throws(HandleConsumedException::class)
    fun hasWalletByPrivateToken(privateToken: String): Boolean {
        return hasWallet(privateToken, true)
    }

    @Throws(HandleConsumedException::class)
    private fun hasWallet(token: String, isPrivate: Boolean): Boolean {
        if (isConsumed) throw HandleConsumedException (
            "Could not check if '$token' (${if (!isPrivate) "pub" else "prt"}) is a wallet of user account '${account.username}': Handle already consumed"
        )

        val wallets = access.walletsByOwnerIdentifier[account.userIdentifier]
            ?: throw IllegalStateException("Missing wallet set for user ${account.username}")

        wallets.forEach { wallet ->
            if (!isPrivate) {
                if (wallet.publicToken == token) return true
            } else {
                if (wallet.privateToken == token) return true
            }
        }

        return false
    }
}