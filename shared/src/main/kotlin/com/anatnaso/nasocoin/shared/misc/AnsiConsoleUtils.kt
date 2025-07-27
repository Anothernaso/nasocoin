package com.anatnaso.nasocoin.shared.misc

import org.fusesource.jansi.AnsiConsole

object AnsiConsoleUtils {
    fun forceInstall() {
        if (!AnsiConsole.isInstalled()) {
            AnsiConsole.systemInstall()
        }
    }
    fun forceUninstall() {
        if (AnsiConsole.isInstalled()) {
            AnsiConsole.systemUninstall()
        }
    }
}
