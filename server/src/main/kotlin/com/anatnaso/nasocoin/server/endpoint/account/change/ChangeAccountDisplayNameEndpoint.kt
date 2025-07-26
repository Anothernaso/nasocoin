package com.anatnaso.nasocoin.server.endpoint.account.change

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import java.io.Serializable
import java.lang.Exception

object ChangeAccountDisplayNameEndpoint {
    private data class RequestPayload (
        @Expose val userIdentifier: String,
        @Expose val password: String,

        @Expose val newDisplayName: String
    ) : Serializable

    fun changeAccountDisplayNameRequestHandle(ctx: Context) {
        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not change display name of user account",
                        "Could not parse request body"
                    )
                )
            return
        }

        if (payload.newDisplayName.isBlank()) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload (
                        "Could not change display name of user account '${payload.userIdentifier}'",
                        "Display name cannot be blank"
                    )
                )
            return
        }

        val db = DatabaseManager.database!!

        val account: UserAccountHandle
        try {
            account = db.getAccountById(payload.userIdentifier)
        } catch (_: NoSuchUserException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json(
                    ErrorPayload (
                        "Could not change display name of user account '${payload.userIdentifier}'",
                        "No such user"
                    )
                )
            return
        }

        if (account.getPassword() != payload.password) {
            ctx
                .status(HttpStatus.UNAUTHORIZED)
                .json(
                    ErrorPayload (
                        "Could not change display name of user account '${account.getUsername()}'",
                        "Invalid password"
                    )
                )
            return
        }

        account.changeDisplayName(payload.newDisplayName)

        ctx.status(HttpStatus.OK)
    }
}