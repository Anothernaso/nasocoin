package com.anatnaso.nasocoin.server.endpoint.get

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.account.UserAccountHandle
import com.anatnaso.nasocoin.server.database.account.exception.NoSuchUserException
import com.anatnaso.nasocoin.server.database.wallet.WalletHandle
import com.anatnaso.nasocoin.server.database.wallet.exception.NoSuchWalletException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import org.slf4j.LoggerFactory
import java.io.Serializable

object GetAccountWalletPrivateTokenEndpoint {
    private data class RequestPayload (
        @Expose val walletPublicToken: String,
        @Expose val ownerAccountPassword: String
    ) : Serializable

    private data class ResponsePayload (
        @Expose val walletPrivateToken: String,
    ) : Serializable

    private val logger = LoggerFactory.getLogger(GetAccountWalletPrivateTokenEndpoint::class.java)

    fun getAccountWalletPrivateTokenRequestHandler(ctx: Context) {
        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not get private token of wallet",
                        "Could not parse request body"
                    )
                )
            return
        }

        val db = DatabaseManager.database!!

        val wallet: WalletHandle
        try {
            wallet = db.getWalletByPublicToken(payload.walletPublicToken)
        } catch (_: NoSuchWalletException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json(
                    ErrorPayload(
                        "Could not get private token of wallet '${payload.walletPublicToken}' (pub)",
                        "No such wallet"
                    )
                )
            return
        }

        val ownerIdentifier = wallet.getOwnerIdentifier()
        val owner: UserAccountHandle
        try {
            owner = db.getAccountById(ownerIdentifier)
        } catch (e: NoSuchUserException) {
            logger.error(
                "Wallet '${payload.walletPublicToken}' has no owner",
                IllegalStateException(
                    "Invalid owner identifier '$ownerIdentifier' of wallet '${payload.walletPublicToken}' (pub)",
                    e
                    )
            )

            ctx
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .json(
                    ErrorPayload(
                        "Could not get private token of wallet '${payload.walletPublicToken}' (pub)",
                        "Wallet '${payload.walletPublicToken}' has no owner"
                    )
                )
            return
        }

        if (owner.getPassword() != payload.ownerAccountPassword) {
            ctx
                .status(HttpStatus.UNAUTHORIZED)
                .json(
                    ErrorPayload(
                        "Could not get private token of wallet '${payload.walletPublicToken}' (pub)",
                        "Invalid password"
                    )
                )
            return
        }

        ctx
            .status(HttpStatus.OK)
            .json(
                ResponsePayload(wallet.getPrivateToken())
            )
    }
}