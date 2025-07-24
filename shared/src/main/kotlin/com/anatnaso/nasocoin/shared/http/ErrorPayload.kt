package com.anatnaso.nasocoin.shared.http

import com.google.gson.annotations.Expose
import java.io.Serializable

data class ErrorPayload(@Expose val error: String, @Expose val details: String = "") : Serializable
