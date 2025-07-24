package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.endpoint.GetAccountUserIdentifierEndpoint
import com.anatnaso.nasocoin.server.endpoint.GetAccountWalletsEndpoint
import io.javalin.Javalin
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(object {}.javaClass.enclosingClass)

fun Javalin.registerNasoCoinEndpoints(): Javalin {

    post("/api/getAccountWallets") { ctx -> GetAccountWalletsEndpoint::getAccountWalletsEndpointHandler }
    get("/api/getAccountUserIdentifier") { ctx -> GetAccountUserIdentifierEndpoint::getAccountUserIdentifierEndpointHandler }

    return this
}
