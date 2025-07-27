package com.anatnaso.nasocoin.client

import com.anatnaso.nasocoin.shared.parser.CommandLineParser

fun main() {
    val parser = CommandLineParser()
        .registerNasoCoinClientCommands()

    println("Type 'exit' to quit and type 'help' to see a list of commands.")
    while (true) {
        println()
        print("> ")
        val input = readLine()

        if (input == null) {
            println("End of input.")
            break
        }

        if (input.lowercase() == "exit") {
            println("Goodbye!")
            break
        }

        parser.parse(input)
    }
}
