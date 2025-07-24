package com.anatnaso.nasocoin.server.config

import com.anatnaso.nasocoin.shared.misc.Globals
import com.google.gson.JsonSyntaxException
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

object ConfigurationManager {
    private val logger = LoggerFactory.getLogger(ConfigurationManager::class.java)
    private val configurationPath = Paths.get("./config.json")

    var configuration = Configuration()

    fun loadConfiguration() {
        if (!Files.exists(configurationPath)) {
            logger.warn("Configuration not found, creating it...")
            saveConfiguration()
            exitProcess(0)
        }

        val configurationJson: String
        try {
            configurationJson = Files.readString(configurationPath)
        } catch (e: IOException) {
            logger.error("A fatal exception occurred: Could not read configuration", e)
            exitProcess(1)
        }

        try {
            configuration = Globals.gson.fromJson(configurationJson, Configuration::class.java)
        } catch (e: JsonSyntaxException) {
            logger.error("A fatal exception occurred: Could not parse config", e)
            exitProcess(1)
        }
    }

    fun saveConfiguration() {
        if (!Files.exists(configurationPath)) {
            if (!Files.exists(configurationPath.parent)) {
                Files.createDirectories(configurationPath.parent)
            }

            Files.createFile(configurationPath)
        }

        val configurationJson = Globals.gson.toJson(configuration)

        try {
            Files.writeString(configurationPath, configurationJson)
        } catch (e: IOException) {
            logger.error("Could not write configuration", e)
            return
        }
    }

    fun initialize() {
        loadConfiguration()
    }
}