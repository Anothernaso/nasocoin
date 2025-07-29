package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.TransferAccountWalletFundsEndpoints
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountDisplayNameEndpoint
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountPasswordEndpoint
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountUsernameEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountDisplayNameEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.CreateAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.DeleteAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountUserIdentifierEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountUsernameEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountWalletFundsEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountWalletPrivateTokenEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountWalletPublicTokenEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.GetAccountWalletsEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.CreateAccountWalletEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.DeleteAccountWallet
import com.anatnaso.nasocoin.server.endpoint.misc.ConnectionTestEndpoint
import com.anatnaso.nasocoin.server.endpoint.misc.LoginTestEndpoint
import io.javalin.Javalin

fun Javalin.registerNasoCoinEndpoints(): Javalin {

    head("/api/connectionTest", ConnectionTestEndpoint::connectionTestRequestHandler)
    post("/api/loginTest", LoginTestEndpoint::loginTestRequestHandler)

    post("/api/createAccount", CreateAccountEndpoint::createAccountRequestHandler)
    delete("/api/deleteAccount", DeleteAccountEndpoint::deleteAccountRequestHandler)

    patch("/api/changeAccountDisplayName", ChangeAccountDisplayNameEndpoint::changeAccountDisplayNameRequestHandle)
    patch("/api/changeAccountUsername", ChangeAccountUsernameEndpoint::changeAccountUsernameRequestHandler)
    patch("/api/changeAccountPassword", ChangeAccountPasswordEndpoint::changeAccountPasswordRequestHandler)

    get("/api/getAccountDisplayName", GetAccountDisplayNameEndpoint::getAccountDisplayNameRequestHandler)
    get("/api/getAccountUsername", GetAccountUsernameEndpoint::getAccountUsernameRequestHandler)
    get("/api/getAccountUserIdentifier", GetAccountUserIdentifierEndpoint::getAccountUserIdentifierRequestHandler)
    post("/api/getAccountWallets", GetAccountWalletsEndpoint::getAccountWalletsRequestHandler)

    post("/api/createAccountWallet", CreateAccountWalletEndpoint::createAccountWalletRequestHandler)
    delete("/api/deleteAccountWallet", DeleteAccountWallet::deleteAccountWalletRequestHandler)

    post("/api/getAccountWalletPrivateToken", GetAccountWalletPrivateTokenEndpoint::getAccountWalletPrivateTokenRequestHandler)
    post("/api/getAccountWalletPublicToken", GetAccountWalletPublicTokenEndpoint::getAccountWalletPublicTokenRequestHandler)
    post("/api/getAccountWalletFunds", GetAccountWalletFundsEndpoint::getAccountWalletFundsRequestHandler)
    patch("/api/transferAccountWalletFunds", TransferAccountWalletFundsEndpoints::transferAccountWalletFundsRequestHandler)

    return this
}
