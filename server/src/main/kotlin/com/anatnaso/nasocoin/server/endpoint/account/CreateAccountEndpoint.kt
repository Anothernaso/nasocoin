package com.anatnaso.nasocoin.server.endpoint.account
import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.UsernameOccupiedException
import com.anatnaso.nasocoin.shared.misc.Globals
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.anatnaso.nasocoin.shared.misc.UsernameValidator
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import java.io.Serializable
import java.lang.Exception

object CreateAccountEndpoint {
    private data class RequestPayload (
        @Expose val displayName: String,
        @Expose val username: String,
        @Expose val password: String,
    ) : Serializable

    private data class ResponsePayload (
        @Expose val userIdentifier: String
    ) : Serializable

    fun createAccountRequestHandler(ctx: Context) {
        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not create user account",
                        "Could not parse request body"
                    )
                )
            return
        }

        if (payload.username.isBlank()) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload("Could not create user account", "Username cannot be blank")
                )
            return
        }

        if (payload.password.isBlank()) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload("Could not create user account '${payload.username}'", "Password cannot be blank")
                )
            return
        }

        if (payload.displayName.isBlank()) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload("Could not create user account '${payload.username}'", "Display name cannot be blank")
                )
            return
        }

        val trimmedDisplayName = payload.displayName.trim()
        val trimmedUsername = payload.username.trim()
        val trimmedPassword = payload.password.trim()

        val usernameErrors = UsernameValidator.validateUsername(trimmedUsername)
        if (!usernameErrors.isEmpty()) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload (
                        "Could not create user account '$trimmedUsername', errors:\n${Globals.gson.toJson(usernameErrors)}",
                        "Usernames can only contain letters, digits, dashes and underscores"
                    )
                )
            return
        }

        val db = DatabaseManager.database

        val account: UserAccountHandle
        try {
            account = db.registerAccount (
                trimmedDisplayName,
                trimmedUsername,
                trimmedPassword
            )
        } catch (_: UsernameOccupiedException) {
            ctx
                .status(HttpStatus.CONFLICT)
                .json(
                    ErrorPayload (
                        "Could not create user account '$trimmedUsername'",
                        "Username occupied"
                    )
                )
            return
        }

        ctx.status(HttpStatus.OK).json(ResponsePayload(account.getUserIdentifier()))
    }
}