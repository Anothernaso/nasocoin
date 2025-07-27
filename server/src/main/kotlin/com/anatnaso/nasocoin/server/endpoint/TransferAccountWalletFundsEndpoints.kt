package com.anatnaso.nasocoin.server.endpoint

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.database.wallet.WalletHandle
import com.anatnaso.nasocoin.server.database.wallet.exception.NoSuchWalletException
import com.anatnaso.nasocoin.shared.http.ErrorPayload
import com.google.gson.annotations.Expose
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import java.io.Serializable
import java.math.BigInteger

object TransferAccountWalletFundsEndpoints {
    private data class RequestPayload(
        @Expose val sourceWalletPrivateToken: String,
        @Expose val destinationWalletPublicToken: String,
        @Expose val transactionAmount: String,
    ) : Serializable

    fun transferAccountWalletFundsRequestHandler(ctx: Context) {
        val payload: RequestPayload
        try {
            payload = ctx.bodyAsClass<RequestPayload>()
        } catch (_: Exception) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not transfer from wallet to wallet",
                        "Could not parse request body"
                    )
                )
            return
        }

        val transactionAmount: BigInteger
        try {
            transactionAmount = BigInteger(payload.transactionAmount)
        } catch (_: NumberFormatException) {
            ctx
                .status(HttpStatus.BAD_REQUEST)
                .json(
                    ErrorPayload(
                        "Could not transfer funds from wallet '${payload.sourceWalletPrivateToken}' (prv) to wallet '${payload.destinationWalletPublicToken}' (pub)",
                        "Transaction amount '${payload.transactionAmount}' is not a valid integer"
                    )
                )
            return
        }

        val db = DatabaseManager.database!!

        val sourceWallet: WalletHandle
        try {
            sourceWallet = db.getWalletByPrivateToken(payload.sourceWalletPrivateToken)
        } catch (_: NoSuchWalletException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json(
                    ErrorPayload(
                        "Could not transfer funds from wallet '${payload.sourceWalletPrivateToken}' (prv) to wallet '${payload.destinationWalletPublicToken}' (pub)",
                        "No such source wallet"
                    )
                )
            return
        }

        val destinationWallet: WalletHandle
        try {
            destinationWallet = db.getWalletByPublicToken(payload.sourceWalletPrivateToken)
        } catch (_: NoSuchWalletException) {
            ctx
                .status(HttpStatus.NOT_FOUND)
                .json(
                    ErrorPayload(
                        "Could not transfer funds from wallet '${payload.sourceWalletPrivateToken}' (prv) to wallet '${payload.destinationWalletPublicToken}' (pub)",
                        "No such destination wallet"
                    )
                )
            return
        }

        if (sourceWallet.getWalletFunds() < transactionAmount) {
            ctx
                .status(HttpStatus.PAYMENT_REQUIRED)
                .json(
                    ErrorPayload(
                        "Could not transfer funds from wallet '${payload.sourceWalletPrivateToken}' (prv) to wallet '${payload.destinationWalletPublicToken}' (pub)",
                        "Not enough funds"
                    )
                )
            return
        }

        sourceWallet.getWalletFunds().subtract(transactionAmount)
        destinationWallet.getWalletFunds().add(transactionAmount)

        ctx
            .status(HttpStatus.OK)
    }
}