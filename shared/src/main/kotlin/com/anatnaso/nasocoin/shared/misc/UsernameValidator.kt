package com.anatnaso.nasocoin.shared.misc

object UsernameValidator {

    enum class UsernameError {
        ContainsSpaces,
        ContainsSpecialCharacters,
    }

    fun validateUsername(username: String): ArrayList<UsernameError> {
        val errors = arrayListOf<UsernameError>()

        // Disallow spaces
        if (username.any { it == ' ' }) {
            errors.add(UsernameError.ContainsSpaces)
        }

        // Disallow all other characters besides letters A to Z, numbers 0 to 9, '_' and '-'
        if (username.any { !it.isLetterOrDigit() && it != '_' && it != '-' }) {
            errors.add(UsernameError.ContainsSpecialCharacters)
        }

        return errors
    }
}