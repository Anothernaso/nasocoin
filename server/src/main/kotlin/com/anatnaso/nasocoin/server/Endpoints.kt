package com.anatnaso.nasocoin.server

import io.javalin.Javalin
import io.javalin.http.HttpStatus
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(object {}.javaClass.enclosingClass)

fun registerEndpoints(app: Javalin): Javalin {

    app.get("/{name}") { ctx ->
        logger.info("Incoming request at '${ctx.path()}'")

        val name = ctx.queryParam("name")
        if (name == null) {
            ctx.status(HttpStatus.BAD_REQUEST).result("Missing 'name' query parameter")
            return@get
        }

        ctx.result("Hello, ${name}!")
    }
    return app
}
