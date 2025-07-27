package com.anatnaso.nasocoin.client

import com.anatnaso.nasocoin.shared.parser.CommandLineParser

fun CommandLineParser.registerNasoCoinClientCommands(): CommandLineParser {
    registerCommand(CommandLineParser.Command(
        "help",
        "Shows this menu",
        arrayListOf(),
    ) { _, _ ->
        printHelp()
    })

    return this
}
