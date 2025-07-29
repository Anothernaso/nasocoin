package com.anatnaso.nasocoin.client.connection

import com.anatnaso.nasocoin.client.connection.exception.ServerConnectionException
import com.anatnaso.nasocoin.shared.http.Endpoints
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.head
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ConnectionManager {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var serverAddrezz: String? = null

    @Throws(ServerConnectionException::class)
    fun serverAddress(): String = serverAddrezz
        ?: throw ServerConnectionException("Not connected to any server")


    suspend fun connectToServer(serverAddress: String): Boolean {
        if (client.head("https://$serverAddress${Endpoints.CONNECTION_TEST}").status == HttpStatusCode.OK) {
            serverAddrezz = serverAddress
            return true
        } else {
            return false
        }
    }
}