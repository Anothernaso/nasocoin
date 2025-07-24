package com.anatnaso.nasocoin.server.middleware

import io.javalin.http.Context
import org.slf4j.LoggerFactory

object BeforeRequestMiddleware {
    private val logger = LoggerFactory.getLogger(BeforeRequestMiddleware::class.java)

    fun beforeRequestHandler(ctx: Context) {
        logger.info("Incoming request at '${ctx.url()}': ${ctx.body()}")
    }
}