package com.anatnaso.nasocoin.server.database

import com.anatnaso.nasocoin.server.database.account.UserAccount
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.server.database.account.exception.UsernameOccupiedException
import com.anatnaso.nasocoin.server.database.wallet.Wallet
import org.apache.commons.codec.digest.DigestUtils
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

data class Database (
    private val access: DatabaseDirectAccess = DatabaseDirectAccess()
) : Serializable {

    companion object {
        private val serialVersionUID: Long = 2L
    }

    @Throws(UsernameOccupiedException::class)
    fun registerAccount(displayName: String, username: String, password: String): UserAccountHandle {
        val userIdentifier = DigestUtils.sha256Hex(username)

        if (access.accounts.containsKey(userIdentifier)) {
            throw UsernameOccupiedException(
                "Could not register user account '$userIdentifier': " +
                        "Username '$username' occupied"
            )
        }

        val account = UserAccount(displayName, username, password)
        access.accounts.put(account.userIdentifier, account)

        access.walletsByOwnerIdentifier.put(account.userIdentifier, ConcurrentHashMap.newKeySet())

        return UserAccountHandle(account, this, access)
    }

    @Throws(NoSuchUserException::class)
    fun getAccountById(userIdentifier: String): UserAccountHandle {
        val account = access.accounts[userIdentifier]
            ?: throw NoSuchUserException("Could not get user account '$userIdentifier': No such user")

        return UserAccountHandle(account, this, access)
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
        return access.accounts.containsKey(userIdentifier);
    }

    fun accountExistsByUsername(username: String): Boolean {
        return accountExistsByIdentifier(DigestUtils.sha256Hex(username))
    }

    data class DatabaseDirectAccess (
        val accounts: ConcurrentHashMap<String, UserAccount> = ConcurrentHashMap<String, UserAccount>(),
        val walletsByPublicToken: ConcurrentHashMap<String, Wallet> = ConcurrentHashMap<String, Wallet>(),
        val walletsByPrivateToken: ConcurrentHashMap<String, Wallet> = ConcurrentHashMap<String, Wallet>(),
        val walletsByOwnerIdentifier: ConcurrentHashMap<String, MutableSet<Wallet>> = ConcurrentHashMap<String, MutableSet<Wallet>>(),
    ) : Serializable
}