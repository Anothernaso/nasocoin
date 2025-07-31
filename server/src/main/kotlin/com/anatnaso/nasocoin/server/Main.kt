package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.config.ConfigurationManager
import com.anatnaso.nasocoin.server.database.DatabaseManager
import io.javalin.Javalin

fun main() {
    ConfigurationManager.initialize()
    DatabaseManager.initialize()

    Javalin.create()
        .registerNasoCoinMiddleware()
        .registerNasoCoinEndpoints()
        .start(ConfigurationManager.configuration.hostname, ConfigurationManager.configuration.port.toInt())
}
