package com.anatnaso.nasocoin.client.commandline

import com.anatnaso.nasocoin.shared.misc.AnsiConsoleUtils
import com.anatnaso.nasocoin.shared.misc.UserUtils
import org.fusesource.jansi.Ansi

object CurrentLinePrefix {
    fun currentLinePrefix(): String {
        AnsiConsoleUtils.forceInstall()
        return "${
            Ansi
                .ansi()
                .fgGreen()
                .a("${UserUtils.getLocalUsername()}@${UserUtils.getLocalHostname()}")
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