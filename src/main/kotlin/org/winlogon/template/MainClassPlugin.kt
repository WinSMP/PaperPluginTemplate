package org.winlogon.template

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.arguments.StringArgument

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

class MainClassPlugin : JavaPlugin() {
    val miniMessage = MiniMessage.miniMessage()
    override fun onEnable() {
        saveDefaultConfig()
        registerCommands()
    }

    private fun checkFolia(): Boolean {
        return try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    private fun registerCommands() {
        CommandAPICommand("say")
             .withArguments(StringArgument("text"))
             .executesPlayer(PlayerCommandExecutor { player, args ->
                 // handle code
             })
            .register()
    }

    /**
     * Converts legacy ampersand codes to MiniMessage tags and deserializes the string into a Component.
     *
     * This function supports both legacy codes (e.g. &a, &c) and native MiniMessage tags.
     */
    private fun parseMessage(input: String): Component {
        return miniMessage.deserialize(legacyToMiniMessage(input))
    }

    /**
     * Converts all legacy ampersand color/format codes in the input string to equivalent MiniMessage tags.
     *
     * For example, "&aHello &cWorld" becomes "<green>Hello <red>World".
     */
     private fun legacyToMiniMessage(input: String): String {
         val colorMap = mapOf(
             '0' to "black",
             '1' to "dark_blue",
             '2' to "dark_green",
             '3' to "dark_aqua",
             '4' to "dark_red",
             '5' to "dark_purple",
             '6' to "gold",
             '7' to "gray",
             '8' to "dark_gray",
             '9' to "blue",
             'a' to "green",
             'b' to "aqua",
             'c' to "red",
             'd' to "light_purple",
             'e' to "yellow",
             'f' to "white",
             'k' to "obfuscated",
             'l' to "bold",
             'm' to "strikethrough",
             'n' to "underlined",
             'o' to "italic",
             'r' to "reset"
         )
     
         val regex = "&([0-9a-fk-or])".toRegex(RegexOption.IGNORE_CASE)
         return regex.replace(input) { matchResult ->
             val code = matchResult.groupValues[1].lowercase()
             val tag = colorMap[code.first()]
             if (tag != null) "<$tag>" else matchResult.value
         }
     }
}
