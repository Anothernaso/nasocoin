package com.anatnaso.nasocoin.server.middleware

import io.javalin.http.Context
import org.slf4j.LoggerFactory

object AfterRequestMiddleware {
    private val logger = LoggerFactory.getLogger(AfterRequestMiddleware::class.java)

    fun afterRequestHandler(ctx: Context) {
        logger.info("Outgoing response at '${ctx.url()}': ${ctx.status()}")
    }
}