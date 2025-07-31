package com.anatnaso.nasocoin.shared.commandline

import com.anatnaso.nasocoin.shared.misc.AnsiConsoleUtils
import org.fusesource.jansi.Ansi
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder

class CommandLineShell(private val currentLinePrefix: (() -> String)? = null) {
    private val commands = mutableListOf<Command>()
    private val terminal = TerminalBuilder.builder().system(true).build()
    private val reader = LineReaderBuilder.builder().terminal(terminal).build()

    class Command {
        val command: String
        val description: String
        val arguments: ArrayList<Argument>
        private val callback: (command: Command, args: Map<String, String>) -> Unit

        fun execute(inputArgs: List<String>) {
            if (inputArgs.size != arguments.size) {
                println("Error: Expected ${arguments.size} arguments but got ${inputArgs.size}")
                return
            }

            val argMap = arguments.mapIndexed { index, arg ->
                arg.name to inputArgs[index]
            }.toMap()

            callback(this, argMap)
        }

        constructor(command: String, description: String, arguments: ArrayList<Argument>, callback: (command: Command, args: Map<String, String>) -> Unit) {
            this.command = command
            this.description = description
            @Suppress("UNCHECKED_CAST")
            this.arguments = arguments.clone() as ArrayList<Argument>
            this.callback = callback
        }
    }

    fun registerCommand(command: Command): CommandLineShell {
        commands.add(command)

        return this
    }

    class Argument(val name: String, val description: String)

    private fun parse(input: String) {
        val tokens = input.trim().split(Regex("\\s+"))
        if (tokens.isEmpty()) return

        val commandName = tokens[0]
        val args = tokens.drop(1)

        val command = commands.find { it.command == commandName }

        if (command != null) {
            command.execute(args)
        } else {
            println("Unknown command: $commandName")
        }
    }

    private fun printHelp() {
        println("Available commands:")
        for (cmd in commands) {
            println("- ${cmd.command}: ${cmd.description}")
            for (arg in cmd.arguments) {
                println("    <${arg.name}>: ${arg.description}")
            }
        }
    }

    fun run() {
        var isRunning = true

        registerCommand(Command(
            "help",
            "Shows this menu.",
            arrayListOf(),
        ) { _, _ ->
            printHelp()
        })
        registerCommand(Command(
            "exit",
            "Exits the shell",
            arrayListOf(),
        ) { _, _ ->
            isRunning = false
        })
        AnsiConsoleUtils.forceInstall()
        print(Ansi.ansi().eraseScreen().cursor(0, 0))

        println("Type 'exit' to quit and type 'help' to see a list of commands.")
        while (isRunning) {
            print(currentLinePrefix?.invoke() ?: "> ")
            val input = reader.readLine()

            if (input == null) continue

            parse(input)
            println()
        }

        AnsiConsoleUtils.forceInstall()
        print(Ansi.ansi().eraseScreen().cursor(0, 0).reset())
    }
}