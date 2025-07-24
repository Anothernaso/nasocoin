package com.anatnaso.nasocoin.server.endpoint.wallet

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import java.io.Serializable

object GetAccountWalletsEndpoint {
    private data class RequestPayload(@Expose val password: String) : Serializable
    private data class ResponsePayload(@Expose val wallets: ArrayList<String>) : Serializable

    fun getAccountWalletsRequestHandler(ctx: Context) {

        val userIdentifier = ctx.queryParam("userIdentifier")
        if (userIdentifier == null) {
            ctx.status(HttpStatus.BAD_REQUEST)
                .json(ErrorPayload("Could not get wallets of unknown user", "Missing required query parameter 'userIdentifier'"))
            return
        }

        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload("Could not get wallets of user account $userIdentifier", "Could not parse request body")
                )
            return
        }

        val db = DatabaseManager.database

        val account: UserAccountHandle
        try {
            account = db.getAccountById(userIdentifier)
        } catch (_: NoSuchUserException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json (
                    ErrorPayload("Could not get wallets of user account '$userIdentifier'", "No such user")
                )
            return
        }

        if (account.getPassword() != payload.password) {
            ctx
                .status(HttpStatus.UNAUTHORIZED)
                .json(
                    ErrorPayload("Could not get wallets of user account '${userIdentifier}'", "Invalid password")
                )
            return
        }

        val publicTokens = arrayListOf<String>()

        val wallets = account.getWallets()
        wallets.forEach { wallet ->
            publicTokens.add(wallet.getPublicToken())
        }

        ctx.status(HttpStatus.OK).json(ResponsePayload(publicTokens))
    }
}