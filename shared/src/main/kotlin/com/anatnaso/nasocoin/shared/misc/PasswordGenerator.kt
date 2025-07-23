package com.anatnaso.nasocoin.shared.misc

object PasswordGenerator {
    fun generateRandomPassword(length: Int = 8): String {
        var password = ""
        (0 until length).forEach { _ ->
            password += (32..126).random().toChar()
        }

        return password
    }
}