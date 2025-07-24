package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.config.ConfigurationManager
import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.server.misc.DatabaseDummy
import io.javalin.Javalin

fun main() {
    ConfigurationManager.initialize()
    DatabaseManager.initialize()
    DatabaseDummy.addDummyData()

    registerEndpoints(Javalin.create())
        .start(ConfigurationManager.configuration.hostname, ConfigurationManager.configuration.port.toInt())
}
