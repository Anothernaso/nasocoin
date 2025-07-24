package com.anatnaso.nasocoin.server

import com.anatnaso.nasocoin.server.middleware.AfterRequestMiddleware
import com.anatnaso.nasocoin.server.middleware.BeforeRequestMiddleware
import io.javalin.Javalin

fun Javalin.registerNasoCoinMiddleware(): Javalin {

    before { ctx -> BeforeRequestMiddleware::beforeRequestHandler }
    after { ctx -> AfterRequestMiddleware::afterRequestHandler }

    return this
}
