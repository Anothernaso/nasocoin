package com.anatnaso.nasocoin.server.database

import com.anatnaso.nasocoin.server.config.ConfigurationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.io.*
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread

object DatabaseManager {

    private var databaseField: Database = Database()
    val database: Database
        get() = databaseField

    private var hasInitialized = false

    private val logger = LoggerFactory.getLogger(DatabaseManager::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)

    fun saveDatabase() {
        logger.info("Database save started")
        val startTime = System.currentTimeMillis()

        val databasePath = Paths.get(ConfigurationManager.configuration.databasePath)

        if (!Files.exists(databasePath.parent)) {
            Files.createDirectories(databasePath.parent)
        }

        ObjectOutputStream(FileOutputStream(databasePath.toFile())).use { oos ->
            try {
                oos.writeObject(database)
            } catch (e: NotSerializableException) {
                logger.error("Could not serialize database: Database contains an object that is not serializable", e)
            } catch (e: IOException) {
                logger.error("Could not write serialized database", e)
            }
        }

        val finishTime = System.currentTimeMillis()
        logger.info("Database save finished in ${finishTime - startTime}ms")
    }

    fun loadDatabase() {
        logger.info("Database load started")
        val startTime = System.currentTimeMillis()

        val databasePath = Paths.get(ConfigurationManager.configuration.databasePath)

        if (!Files.exists(databasePath)) {
            logger.warn("Database not found, creating it...")
            saveDatabase()
            return
        }

        ObjectInputStream(FileInputStream(databasePath.toFile())).use { ois ->
            try {
                databaseField = ois.readObject() as Database
            } catch (e: ClassNotFoundException) {
                logger.error("Could not deserialize database", e)
                return
            } catch (e: IOException) {
                logger.error("Could not read database", e)
                return
            }
        }

        val finishTime = System.currentTimeMillis()
        logger.info("Database load finished in ${finishTime - startTime}ms")
    }

    fun initialize() {
        if (hasInitialized) return

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            saveDatabase()
        })

        scope.launch {
            val delayTime = 60 * 60 * 1000L // 1-hour delay

            delay(delayTime)

            while (isActive) {

                saveDatabase()

                delay(delayTime)
            }
        }

        hasInitialized = true

        loadDatabase()
        if (!database.accountExistsByUsername(ConfigurationManager.configuration.rootUsername)) {
            database.registerAccount (
                ConfigurationManager.configuration.rootDisplayName,
                ConfigurationManager.configuration.rootUsername,
                ConfigurationManager.configuration.rootPassword,
            )
        }

        val rootAccount = database.getAccountByUsername (
            ConfigurationManager.configuration.rootUsername
        )

        val rootWallets = rootAccount.getWallets()
        if (rootWallets.isEmpty()) {
            val rootWallet = rootAccount.createWallet()
            rootWallet.getWalletFunds().add(
                BigInteger(ConfigurationManager.configuration.rootDefaultCapital)
            )
        }
    }
}
