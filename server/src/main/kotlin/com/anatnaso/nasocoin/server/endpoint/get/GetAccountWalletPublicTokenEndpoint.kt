package com.anatnaso.nasocoin.server.endpoint.get

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.wallet.WalletHandle
import com.anatnaso.nasocoin.server.database.wallet.exception.NoSuchWalletException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import java.io.Serializable

object GetAccountWalletPublicTokenEndpoint {
    private data class RequestPayload(@Expose val walletPrivateToken: String) : Serializable
    private data class ResponsePayload(@Expose val walletPublicToken: String) : Serializable

    fun getAccountWalletPublicTokenRequestHandler(ctx: Context) {
        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not get public token of wallet",
                        "Could not parse request body"
                    )
                )
            return
        }

        val db = DatabaseManager.database!!

        val wallet: WalletHandle
        try {
            wallet = db.getWalletByPrivateToken(payload.walletPrivateToken)
        } catch (_: NoSuchWalletException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json(
                    ErrorPayload(
                        "Could not get private token of wallet '${payload.walletPrivateToken}' (prv)",
                        "No such wallet"
                    )
                )
            return
        }

        ctx
            .status(HttpStatus.OK)
            .json(
                ResponsePayload(
                    wallet.getPublicToken()
                )
            )
    }
}