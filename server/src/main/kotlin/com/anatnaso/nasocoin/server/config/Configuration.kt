package com.anatnaso.nasocoin.server.config

import com.google.gson.annotations.Expose
import java.io.Serializable

data class Configuration(
    @Expose val hostname: String = "127.0.0.1",
    @Expose val port: Short = 7070,
    @Expose val externalAddress: String = "127.0.0.1:7070",

    @Expose val rootDisplayName: String ="Root",
    @Expose val rootUsername: String = "root",
    @Expose val rootPassword: String = "SuperSecretPassword",
    @Expose val rootDefaultCapital: String = "1000000000",

    @Expose val databasePath: String = "./appdata/database.ser",
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 2L
    }
}
