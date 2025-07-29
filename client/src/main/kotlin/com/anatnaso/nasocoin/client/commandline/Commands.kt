package com.anatnaso.nasocoin.client.commandline

import com.anatnaso.nasocoin.client.connection.ConnectionManager
import com.anatnaso.nasocoin.client.connection.exception.ServerConnectionException
import com.anatnaso.nasocoin.shared.commandline.CommandLineShell
import kotlinx.coroutines.runBlocking

fun CommandLineShell.registerNasoCoinClientCommands(): CommandLineShell {
    registerCommand(CommandLineShell.Command(
        "connect",
        "Connect to a server",
        arrayListOf(
            CommandLineShell.Argument(
                "serverAddress",
                "The address of the server to connect to"
            )
        )
    ) { command, args ->
        val inputAddress = args["serverAddress"]!!
        runBlocking {
            ConnectionManager.connectToServer(inputAddress)
        }

        try {
            val serverAddress = ConnectionManager.serverAddress()
            println("Successfully connected to server '$serverAddress'")
        } catch (_: ServerConnectionException) {
            println("Could not connect to server '$inputAddress'")
        }
    })

    return this
}
