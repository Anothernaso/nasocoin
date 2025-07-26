package com.anatnaso.nasocoin.server.database

import com.anatnaso.nasocoin.server.database.account.UserAccount
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.server.database.account.exception.UsernameOccupiedException
import com.anatnaso.nasocoin.server.database.wallet.Wallet
import com.anatnaso.nasocoin.server.database.wallet.WalletHandle
import com.anatnaso.nasocoin.server.database.wallet.exception.NoSuchWalletException
import org.apache.commons.codec.digest.DigestUtils
import java.io.Serializable
import java.math.BigInteger
import java.util.concurrent.ConcurrentHashMap

class Database : Serializable {

    companion object {
        private const val serialVersionUID: Long = 2L
    }

    private val access: DatabaseDirectAccess = DatabaseDirectAccess()

    constructor(rootDisplayName: String, rootUsername: String, rootPassword: String, rootCapital: String) {
        val rootAccount = createAccount (
                rootDisplayName,
                rootUsername,
                rootPassword,
            )


        val rootWallet = rootAccount.createWallet()
        rootWallet.getWalletFunds().add(
            BigInteger(rootCapital)
        )

        access.rootUserIdentifier = rootAccount.getUserIdentifier()
        access.rootWalletPublicToken = rootWallet.getPublicToken()
    }

    @Throws(UsernameOccupiedException::class)
    fun createAccount(displayName: String, username: String, password: String): UserAccountHandle {
        val userIdentifier = DigestUtils.sha256Hex(username)

        if (access.accounts.containsKey(userIdentifier)) {
            throw UsernameOccupiedException(
                "Could not register user account '$userIdentifier': " +
                        "Username '$username' occupied"
            )
        }

        val account = UserAccount(displayName, username, password)
        access.accounts[account.userIdentifier] = account

        access.walletsByOwnerIdentifier[account.userIdentifier] = ConcurrentHashMap.newKeySet()

        return UserAccountHandle(account, access)
    }

    @Throws(NoSuchUserException::class)
    fun getAccountById(userIdentifier: String): UserAccountHandle {
        val account = access.accounts[userIdentifier]
            ?: throw NoSuchUserException("Could not get user account '$userIdentifier': No such user")

        return UserAccountHandle(account, access)
    }

    @Throws(NoSuchUserException::class)
    fun getAccountByUsername(username: String): UserAccountHandle {
        try {
            return getAccountById(DigestUtils.sha256Hex(username))
        } catch (e: NoSuchUserException) {
            throw NoSuchUserException("Could not get user account '$username': No such user", e)
        }
    }

    fun accountExistsByIdentifier(userIdentifier: String): Boolean {
        return access.accounts.containsKey(userIdentifier)
    }

    fun accountExistsByUsername(username: String): Boolean {
        return accountExistsByIdentifier(DigestUtils.sha256Hex(username))
    }

    @Throws(NoSuchWalletException::class)
    fun getWalletByPublicToken(publicToken: String): WalletHandle {
        val wallet = access.walletsByPublicToken[publicToken]
            ?: throw NoSuchWalletException("Could not get wallet '$publicToken' (pub): No such wallet")

        val owner = access.accounts[wallet.ownerUserIdentifier]
            ?: throw IllegalStateException(
                "Could not get wallet '$publicToken' (pub): Invalid wallet owner user identifier"
            )

        return WalletHandle(wallet, owner, access)
    }

    @Throws(NoSuchWalletException::class)
    fun getWalletByPrivateToken(privateToken: String): WalletHandle {
        val wallet = access.walletsByPrivateToken[privateToken]
            ?: throw NoSuchWalletException("Could not get wallet '$privateToken' (prv): No such wallet")

        val owner = access.accounts[wallet.ownerUserIdentifier]
            ?: throw IllegalStateException(
                "Could not get wallet '$privateToken' (prv): Invalid wallet owner user identifier"
            )

        return WalletHandle(wallet, owner, access)
    }

    fun walletExistsByPublicToken(publicToken: String): Boolean {
        return access.walletsByPublicToken.containsKey(publicToken)
    }

    fun walletExistsByPrivateToken(privateToken: String): Boolean {
        return access.walletsByPrivateToken.containsKey(privateToken)
    }

    data class DatabaseDirectAccess (
        val accounts: ConcurrentHashMap<String, UserAccount> = ConcurrentHashMap<String, UserAccount>(),
        val walletsByPublicToken: ConcurrentHashMap<String, Wallet> = ConcurrentHashMap<String, Wallet>(),
        val walletsByPrivateToken: ConcurrentHashMap<String, Wallet> = ConcurrentHashMap<String, Wallet>(),
        val walletsByOwnerIdentifier: ConcurrentHashMap<String, MutableSet<Wallet>> = ConcurrentHashMap<String, MutableSet<Wallet>>(),

        var rootUserIdentifier: String = "",
        var rootWalletPublicToken: String = "",
    ) : Serializable
}