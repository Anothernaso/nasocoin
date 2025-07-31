package com.anatnaso.nasocoin.client.commandline

import com.anatnaso.nasocoin.client.connection.ConnectionManager
import com.anatnaso.nasocoin.client.connection.exception.ServerConnectionException
import com.anatnaso.nasocoin.shared.misc.AnsiConsoleUtils
import com.anatnaso.nasocoin.shared.misc.UserUtils
import org.fusesource.jansi.Ansi

object CurrentLinePrefix {
    fun currentLinePrefix(): String {
        val first: String = if (
                ConnectionManager.isConnectionActive() && !ConnectionManager.isConnectionLost()
            )
        {
            ConnectionManager.serverAddress()
        } else {
            "Disconnected"
        }

        AnsiConsoleUtils.forceInstall()
        return "${
            Ansi
                .ansi()
                .fgGreen()
                .a(first)
                .reset()
        }:${
            Ansi
                .ansi()
                .fgBlue()
                .a("NasoCoin")
                .reset()
        }$ "
    }
}