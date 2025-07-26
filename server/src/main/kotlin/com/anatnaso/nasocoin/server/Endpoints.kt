package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.account.ChangeAccountDisplayNameEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.ChangeAccountUsernameEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.CreateAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.DeleteAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.GetAccountUserIdentifierEndpoint
import com.anatnaso.nasocoin.server.endpoint.wallet.GetAccountWalletsEndpoint
import io.javalin.Javalin

fun Javalin.registerNasoCoinEndpoints(): Javalin {

    post("/api/createAccount", CreateAccountEndpoint::createAccountRequestHandler)
    delete("/api/deleteAccount", DeleteAccountEndpoint::deleteAccountRequestHandler)

    patch("/api/changeAccountDisplayName", ChangeAccountDisplayNameEndpoint::changeAccountDisplayNameRequestHandle)
    patch("/api/changeAccountUsername", ChangeAccountUsernameEndpoint::changeAccountUsernameRequestHandler)

    post("/api/getAccountWallets", GetAccountWalletsEndpoint::getAccountWalletsRequestHandler)
    get("/api/getAccountUserIdentifier", GetAccountUserIdentifierEndpoint::getAccountUserIdentifierRequestHandler)

    return this
}
