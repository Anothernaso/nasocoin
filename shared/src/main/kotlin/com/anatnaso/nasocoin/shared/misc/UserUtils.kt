package com.anatnaso.nasocoin.shared.misc

import java.net.InetAddress

object UserUtils {
    fun getLocalUsername(): String {
        return System.getProperty("user.name") ?: "Unknown User"
    }

    fun getLocalHostname(): String {
        return try {
            InetAddress.getLocalHost().hostName
        } catch (e: Exception) {
            "Unknown Host"
        }
    }

    fun getCurrentWorkingDirectory(): String {
        return System.getProperty("user.dir") ?: "Unknown Directory"
    }
}