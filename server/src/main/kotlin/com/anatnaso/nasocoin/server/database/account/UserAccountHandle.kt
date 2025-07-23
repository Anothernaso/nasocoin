package com.anatnaso.nasocoin.server.database.account

import com.anatnaso.nasocoin.server.database.Database
import com.anatnaso.nasocoin.server.database.account.exception.UsernameOccupiedException
import com.anatnaso.nasocoin.server.database.exception.HandleConsumedException
import org.apache.commons.codec.digest.DigestUtils

class UserAccountHandle (
    private val account: UserAccount,
    private val database: Database,
    private val access: Database.DatabaseDirectAccess
) {
    private var isConsumed = false

    fun isConsumed(): Boolean = isConsumed

    @Throws(HandleConsumedException::class)
    fun deleteAccount() {
        if (isConsumed) throw HandleConsumedException (
            "Could not delete user account '${account.username}': Handle already consumed"
        )

        // TODO: Delete any associated wallets

        access.accounts.remove(account.userIdentifier)

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

        synchronized(access.accounts) {
            access.accounts.remove(account.userIdentifier)
            account.username = username // This will automatically generate a new user identifier

            // TODO: Relink wallet keys to the new user identifier
            //       that was automatically generated when changing the username

            access.accounts.put(account.userIdentifier, account)
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
}