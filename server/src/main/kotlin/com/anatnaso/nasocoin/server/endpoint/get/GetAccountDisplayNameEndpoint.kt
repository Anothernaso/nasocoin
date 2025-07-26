package com.anatnaso.nasocoin.server.endpoint.get

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import java.io.Serializable

object GetAccountDisplayNameEndpoint {
    private data class ResponsePayload(@Expose val displayName: String) : Serializable

    fun getAccountDisplayNameRequestHandler(ctx: Context) {
        val userIdentifier = ctx.queryParam("userIdentifier")
        if (userIdentifier == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not get display name of user account",
                        "Missing required query parameter 'userIdentifier'"
                    )
                )
            return
        }

        val db = DatabaseManager.database!!

        val account: UserAccountHandle
        try {
            account = db.getAccountById(userIdentifier)
        } catch (_: NoSuchUserException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json (
                    ErrorPayload(
                        "Could not get display name of user account '${userIdentifier}'",
                        "No such user"
                    )
                )
            return
        }

        ctx
            .status(HttpStatus.OK)
            .json(
                ResponsePayload (
                    account.getDisplayName()
                )
            )
    }
}