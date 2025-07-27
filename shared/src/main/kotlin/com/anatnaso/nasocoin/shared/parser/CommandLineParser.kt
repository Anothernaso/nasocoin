package com.anatnaso.nasocoin.shared.parser

class CommandLineParser {
    private val commands = mutableListOf<Command>()

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
            this.arguments = arguments.clone() as ArrayList<Argument>
            this.callback = callback
        }
    }

    fun registerCommand(command: Command): CommandLineParser {
        commands.add(command)

        return this
    }

    class Argument(val name: String, val description: String)

    fun parse(input: String) {
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

    fun printHelp() {
        println("Available commands:")
        for (cmd in commands) {
            println("- ${cmd.command}: ${cmd.description}")
            for (arg in cmd.arguments) {
                println("    <${arg.name}>: ${arg.description}")
            }
        }
    }
}