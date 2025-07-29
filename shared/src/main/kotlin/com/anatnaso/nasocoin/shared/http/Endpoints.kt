package com.anatnaso.nasocoin.shared.http

object Endpoints {
    const val CONNECTION_TEST = "/api/connectionTest"
    const val LOGIN_TEST = "/api/loginTest"

    const val CREATE_ACCOUNT = "/api/createAccount"
    const val DELETE_ACCOUNT = "/api/deleteAccount"

    const val CHANGE_ACCOUNT_DISPLAY_NAME = "/api/changeAccountDisplayName"
    const val CHANGE_ACCOUNT_USERNAME = "/api/changeAccountUsername"
    const val CHANGE_ACCOUNT_PASSWORD = "/api/changeAccountPassword"

    const val GET_ACCOUNT_DISPLAY_NAME = "/api/getAccountDisplayName"
    const val GET_ACCOUNT_USERNAME = "/api/getAccountUsername"
    const val GET_ACCOUNT_USER_IDENTIFIER = "/api/getAccountUserIdentifier"
    const val GET_ACCOUNT_WALLETS = "/api/getAccountWallets"

    const val CREATE_ACCOUNT_WALLET = "/api/createAccountWallet"
    const val DELETE_ACCOUNT_WALLET = "/api/deleteAccountWallet"

    const val GET_ACCOUNT_WALLET_PUBLIC_TOKEN = "/api/getAccountWalletPublicToken"
    const val GET_ACCOUNT_WALLET_PRIVATE_TOKEN = "/api/getAccountWalletPrivateToken"
    const val GET_ACCOUNT_WALLET_FUNDS = "/api/getAccountWalletFunds"
    const val TRANSFER_ACCOUNT_WALLET_FUNDS = "/api/transferAccountWalletFunds"
}