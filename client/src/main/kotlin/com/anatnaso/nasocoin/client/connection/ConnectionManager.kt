package com.anatnaso.nasocoin.client.connection

import com.anatnaso.nasocoin.client.connection.exception.ServerConnectionException
import com.anatnaso.nasocoin.shared.http.Endpoints
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

object ConnectionManager {

    private var client: HttpClient? = null

    private var isConnectionActive = false
    private var isConnectionLost = false

    private var serverAddress: String? = null

    @Throws(ServerConnectionException::class)
    fun client() = client
        ?: throw ServerConnectionException("Could not get client: Not connected to any server")

    fun isConnectionActive() = isConnectionActive
    fun isConnectionLost() = isConnectionLost

    @Throws(ServerConnectionException::class)
    fun serverAddress() = serverAddress ?: throw ServerConnectionException("Could not get server address: Not connected to any server")

    val scope = {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            while (true) {
                delay(1000)

                if (!isConnectionActive) {
                    continue
                }

                val url = "${ServerUrlUtils.getServerUrl(serverAddress!!)}${Endpoints.CONNECTION_TEST}"

                try {
                    client!!.head(url)
                    isConnectionLost = false
                } catch (_: Exception) {
                    isConnectionLost = true
                }
            }
        }

        scope
    }()

    @Throws(ServerConnectionException::class)
    fun connect(address: String) {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val url = "${ServerUrlUtils.getServerUrl(address)}${Endpoints.CONNECTION_TEST}"

        try {
            runBlocking {
                client!!.head(url).status
            }
        } catch (e: Exception) {
            client!!.close()
            client = null

            throw ServerConnectionException("Could not connect to server '$address'", e)
        }

        isConnectionActive = true
        isConnectionLost = false

        serverAddress = address
    }

    @Throws(ServerConnectionException::class)
    fun disconnect() {
        if (!isConnectionActive) {
            throw ServerConnectionException("Could not disconnect from server: No connected to any server")
        }

        client!!.close()
        client = null

        isConnectionActive = false
        isConnectionLost = false

        serverAddress = null
    }
}