package com.anatnaso.nasocoin.server.endpoint.account

import com.google.gson.annotations.Expose
import io.javalin.http.Context
import java.io.Serializable

object DeleteAccountEndpoint {
    private data class RequestPayload (
        @Expose val username: String,
        @Expose val password: String,
    ) : Serializable

    fun deleteAccountRequestHandler(ctx: Context) {

    }
}