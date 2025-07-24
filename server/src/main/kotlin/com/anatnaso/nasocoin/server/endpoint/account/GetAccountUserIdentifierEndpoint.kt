package com.anatnaso.nasocoin.server.endpoint.account

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import java.io.Serializable

object GetAccountUserIdentifierEndpoint {
    private data class ResponsePayload(@Expose val userIdentifier: String) : Serializable

    fun getAccountUserIdentifierRequestHandler(ctx: Context) {

        val username = ctx.queryParam("username")
        if (username == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                .json(ErrorPayload("Could not get user identifier of unknown user account", "Missing required query parameter 'username'"))
            return
        }

        val db = DatabaseManager.database

        val account: UserAccountHandle
        try {
            account = db.getAccountByUsername(username)
        } catch (_: NoSuchUserException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json (
                    ErrorPayload("Could not get user identifier of user account '${username}'", "No such user")
                )
            return
        }

        ctx.status(HttpStatus.OK).json(ResponsePayload(account.getUserIdentifier()))
    }
}