package com.anatnaso.nasocoin.shared.misc

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Globals {
    val gson: Gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    val gsonPretty: Gson = GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
}