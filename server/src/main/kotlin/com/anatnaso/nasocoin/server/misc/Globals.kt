package com.anatnaso.nasocoin.server.misc

import com.google.gson.GsonBuilder

object Globals {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}