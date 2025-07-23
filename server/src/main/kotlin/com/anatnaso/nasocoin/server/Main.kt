package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.misc.DatabaseDummy
import io.javalin.Javalin
import io.javalin.http.HttpStatus
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(object {}.javaClass.enclosingClass)

fun main() {
    DatabaseManager.initialize()
    DatabaseDummy.addDummyData()

    Javalin.create()
        .get("/{name}") { ctx ->
            logger.info("Incoming request at '${ctx.path()}'")

            val name = ctx.queryParam("name")
            if (name == null) {
                ctx.status(HttpStatus.BAD_REQUEST).result("Missing 'name' query parameter")
                return@get
            }

            ctx.result("Hello, ${name}!")
        }
        .start("0.0.0.0", 7070)
}
