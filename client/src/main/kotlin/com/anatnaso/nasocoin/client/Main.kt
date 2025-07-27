package com.anatnaso.nasocoin.client

import com.anatnaso.nasocoin.shared.commandline.CommandLineShell
import com.anatnaso.nasocoin.shared.misc.AnsiConsoleUtils
import org.fusesource.jansi.Ansi
import kotlin.concurrent.thread

fun main() {
    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        AnsiConsoleUtils.forceInstall()
        print(
            Ansi
                .ansi()
                .eraseScreen()
                .cursor(0, 0)
                .reset()
        )
    })

    CommandLineShell(CurrentLinePrefix::currentLinePrefix)
        .registerNasoCoinClientCommands()
        .run()
}
