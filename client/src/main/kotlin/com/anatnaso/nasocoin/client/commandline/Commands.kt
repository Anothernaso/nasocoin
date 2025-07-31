package com.anatnaso.nasocoin.client.commandline

import com.anatnaso.nasocoin.client.connection.ConnectionManager
import com.anatnaso.nasocoin.client.connection.exception.ServerConnectionException
import com.anatnaso.nasocoin.shared.commandline.CommandLineShell
import com.anatnaso.nasocoin.shared.misc.AnsiConsoleUtils
import org.fusesource.jansi.Ansi

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
        AnsiConsoleUtils.forceInstall()

        val inputAddress = args["serverAddress"]!!
        ConnectionManager.connect(inputAddress)

        try {
            val serverAddress = ConnectionManager.serverAddress()
            println(
                Ansi.ansi()
                    .fgGreen()
                    .a("Successfully connected to server '$serverAddress'")
                    .reset()
            )
        } catch (_: ServerConnectionException) {
            println(
                Ansi.ansi()
                    .fgRed()
                    .a("Could not connect to server '$inputAddress'")
                    .reset()
            )
        }
    })

    registerCommand(CommandLineShell.Command(
        "disconnect",
        "Disconnects from the current server",
        arrayListOf(),
    ) { _, _ ->
        AnsiConsoleUtils.forceInstall()

        try {
            ConnectionManager.disconnect()
        } catch (_: ServerConnectionException) {
            println(
                Ansi.ansi()
                    .fgRed()
                    .a("Could not disconnect from server: Not connected to any server")
                    .reset()
            )
            return@Command
        }

        println(
            Ansi.ansi()
                .fgGreen()
                .a("Successfully disconnected from server")
                .reset()
        )
    })

    return this
}
