package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.TransferAccountWalletFundsEndpoints
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountDisplayNameEndpoint
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountPasswordEndpoint
import com.anatnaso.nasocoin.server.endpoint.change.ChangeAccountUsernameEndpoint
import com.anatnaso.nasocoin.server.endpoint.get.*
import com.anatnaso.nasocoin.server.endpoint.lifetime.CreateAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.CreateAccountWalletEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.DeleteAccountEndpoint
import com.anatnaso.nasocoin.server.endpoint.lifetime.DeleteAccountWallet
import com.anatnaso.nasocoin.server.endpoint.misc.ConnectionTestEndpoint
import com.anatnaso.nasocoin.server.endpoint.misc.LoginTestEndpoint
import com.anatnaso.nasocoin.shared.http.Endpoints
import io.javalin.Javalin

fun Javalin.registerNasoCoinEndpoints(): Javalin {

    head(Endpoints.CONNECTION_TEST, ConnectionTestEndpoint::connectionTestRequestHandler)
    post(Endpoints.LOGIN_TEST, LoginTestEndpoint::loginTestRequestHandler)

    post(Endpoints.CREATE_ACCOUNT, CreateAccountEndpoint::createAccountRequestHandler)
    delete(Endpoints.DELETE_ACCOUNT, DeleteAccountEndpoint::deleteAccountRequestHandler)

    patch(Endpoints.CHANGE_ACCOUNT_DISPLAY_NAME, ChangeAccountDisplayNameEndpoint::changeAccountDisplayNameRequestHandle)
    patch(Endpoints.CHANGE_ACCOUNT_USERNAME, ChangeAccountUsernameEndpoint::changeAccountUsernameRequestHandler)
    patch(Endpoints.CHANGE_ACCOUNT_PASSWORD, ChangeAccountPasswordEndpoint::changeAccountPasswordRequestHandler)

    get(Endpoints.GET_ACCOUNT_DISPLAY_NAME, GetAccountDisplayNameEndpoint::getAccountDisplayNameRequestHandler)
    get(Endpoints.GET_ACCOUNT_USERNAME, GetAccountUsernameEndpoint::getAccountUsernameRequestHandler)
    get(Endpoints.GET_ACCOUNT_USER_IDENTIFIER, GetAccountUserIdentifierEndpoint::getAccountUserIdentifierRequestHandler)
    post(Endpoints.GET_ACCOUNT_WALLETS, GetAccountWalletsEndpoint::getAccountWalletsRequestHandler)

    post(Endpoints.CREATE_ACCOUNT_WALLET, CreateAccountWalletEndpoint::createAccountWalletRequestHandler)
    delete(Endpoints.DELETE_ACCOUNT_WALLET, DeleteAccountWallet::deleteAccountWalletRequestHandler)

    post(Endpoints.GET_ACCOUNT_WALLET_PUBLIC_TOKEN, GetAccountWalletPublicTokenEndpoint::getAccountWalletPublicTokenRequestHandler)
    post(Endpoints.GET_ACCOUNT_WALLET_PRIVATE_TOKEN, GetAccountWalletPrivateTokenEndpoint::getAccountWalletPrivateTokenRequestHandler)
    post(Endpoints.GET_ACCOUNT_WALLET_FUNDS, GetAccountWalletFundsEndpoint::getAccountWalletFundsRequestHandler)
    patch(Endpoints.TRANSFER_ACCOUNT_WALLET_FUNDS, TransferAccountWalletFundsEndpoints::transferAccountWalletFundsRequestHandler)

    return this
}
