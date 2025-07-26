package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.account.change.ChangeAccountDisplayNameEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.change.ChangeAccountUsernameEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.lifetime.CreateAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.lifetime.DeleteAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.account.get.GetAccountUserIdentifierEndpoint
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
