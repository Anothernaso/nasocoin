package com.anatnaso.nasocoin.server.database.account

import org.apache.commons.codec.digest.DigestUtils
import java.io.Serializable

@ConsistentCopyVisibility
data class UserAccount private constructor(
    private var displayNameField: String,
    private var usernameField: String,
    private var userIdentifierField: String,
    private var passwordField: String
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 2L
    }

    constructor(displayName: String, username: String, password: String) : this(
        displayName,
        username,
        DigestUtils.sha256Hex(username),
        password
    )

    var displayName: String
        get() = displayNameField
        set(value) { displayNameField = value }

    var username: String
        get() = usernameField
        set(value) {
            usernameField = value
            userIdentifierField = DigestUtils.sha256Hex(value)
        }

    val userIdentifier: String
        get() = userIdentifierField

    var password: String
        get() = passwordField
        set(value) { passwordField = value }
}
