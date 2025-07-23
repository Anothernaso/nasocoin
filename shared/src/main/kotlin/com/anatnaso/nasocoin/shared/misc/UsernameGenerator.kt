package com.anatnaso.nasocoin.shared.misc

object UsernameGenerator {
    fun generateUsername(name: String): String = name.replace(' ', '_').lowercase()
    fun generateRandomUsername(): String = generateUsername(NameGenerator.generateRandomName())
}