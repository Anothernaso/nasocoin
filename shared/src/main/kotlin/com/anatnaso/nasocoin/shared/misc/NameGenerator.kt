package com.anatnaso.nasocoin.shared.misc

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.random.Random

object NameGenerator {
    const val GROUP = "com/anatnaso/nasocoin/shared/"

    private val primaryNames: List<String> = loadNames(GROUP + "firstnames.txt")
    private val secondaryNames: List<String> = loadNames(GROUP + "middlenames.txt")
    private val lastNames: List<String> = loadNames(GROUP + "lastnames.txt")

    private fun loadNames(filename: String): List<String> {
        val inputStream = this::class.java.classLoader.getResourceAsStream(filename)
            ?: throw IllegalArgumentException("Resource file $filename not found")
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            return reader.lineSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .toList()
        }
    }

    fun generateRandomName(): String {
        val firstName = primaryNames.random()
        val lastName = lastNames.random()

        val includeSecondary = Random.nextDouble() < 0.3
        return if (includeSecondary && secondaryNames.isNotEmpty()) {
            val secondaryName = secondaryNames.random()
            "$firstName $secondaryName $lastName"
        } else {
            "$firstName $lastName"
        }
    }
}