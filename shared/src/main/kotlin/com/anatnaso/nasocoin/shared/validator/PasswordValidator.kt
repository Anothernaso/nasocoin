package com.anatnaso.nasocoin.shared.validator

import java.io.Serializable

object PasswordValidator {
    enum class PasswordError : Serializable {
        ContainsSpaces,
        ContainsEscapeCodes,
    }

    fun validatePassword(password: String): ArrayList<PasswordError> {
        val errors = arrayListOf<PasswordError>()

        // Disallow spaces
        if (password.any { it == ' ' }) {
            errors.add(PasswordError.ContainsSpaces)
        }

        // Disallow ANSI escape codes/non-printable characters
        if (password.any { it in '\u0000'..'\u001F' || it == '\u007F' }) {
            errors.add(PasswordError.ContainsEscapeCodes)
        }

        return errors
    }
}