package com.anatnaso.nasocoin.client.connection

object ServerUrlUtils {
    fun getServerUrl(serverAddress: String): String {
        return "http://$serverAddress"
    }
}