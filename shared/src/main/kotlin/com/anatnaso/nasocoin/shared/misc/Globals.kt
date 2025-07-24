package com.anatnaso.nasocoin.shared.misc

import com.google.gson.GsonBuilder

object Globals {
    val gson = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}