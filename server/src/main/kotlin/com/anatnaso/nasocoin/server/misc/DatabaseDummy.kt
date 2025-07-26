package com.anatnaso.nasocoin.server.misc

import com.anatnaso.nasocoin.server.database.DatabaseManager
import com.anatnaso.nasocoin.shared.misc.NameGenerator
import com.anatnaso.nasocoin.shared.misc.PasswordGenerator
import com.anatnaso.nasocoin.shared.misc.UsernameGenerator

object DatabaseDummy {
    fun addDummyData() {
        val db = DatabaseManager.database

        (0 until 1000).forEach { i ->
            val displayName = NameGenerator.generateRandomName()
            val username = UsernameGenerator.generateUsername(displayName)
            val password = PasswordGenerator.generateRandomPassword()

            if (db!!.accountExistsByUsername(username)) return@forEach
            db.createAccount(displayName, username, password)
        }
    }
}