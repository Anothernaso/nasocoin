package com.anatnaso.nasocoin.server.endpoint.misc

import io.javalin.http.Context
import io.javalin.http.HttpStatus

object ConnectionTestEndpoint {
    fun connectionTestRequestHandler(ctx: Context) {
        ctx
            .status(HttpStatus.OK)
    }
}