package com.anatnaso.nasocoin.client

import com.anatnaso.nasocoin.shared.commandline.CommandLineShell

fun main() {
    val parser = CommandLineShell()
        .registerNasoCoinClientCommands()
        .run()
}
