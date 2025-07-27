package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.config.ConfigurationManager
import com.anatnaso.nasocoin.server.database.DatabaseManager
//import com.anatnaso.nasocoin.server.misc.DatabaseDummy
import io.javalin.Javalin
import org.fusesource.jansi.AnsiConsole

fun main() {
    if (!AnsiConsole.isInstalled()) {
        AnsiConsole.systemInstall()
    }

    ConfigurationManager.initialize()
    DatabaseManager.initialize()
    //DatabaseDummy.addDummyData()

    Javalin.create()
        .registerNasoCoinMiddleware()
        .registerNasoCoinEndpoints()
        .start(ConfigurationManager.configuration.hostname, ConfigurationManager.configuration.port.toInt())
}
