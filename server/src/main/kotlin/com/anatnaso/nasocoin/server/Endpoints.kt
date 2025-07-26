package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.account.CreateAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.DeleteAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.GetAccountUserIdentifierEndpoint
import com.anatnaso.nasocoin.server.endpoint.wallet.GetAccountWalletsEndpoint
import io.javalin.Javalin

fun Javalin.registerNasoCoinEndpoints(): Javalin {

    post("/api/createAccount", CreateAccountEndpoint::createAccountRequestHandler)
    post("/api/deleteAccount", DeleteAccountEndpoint::deleteAccountRequestHandler)

    post("/api/getAccountWallets", GetAccountWalletsEndpoint::getAccountWalletsRequestHandler)
    get("/api/getAccountUserIdentifier", GetAccountUserIdentifierEndpoint::getAccountUserIdentifierRequestHandler)

    return this
}
