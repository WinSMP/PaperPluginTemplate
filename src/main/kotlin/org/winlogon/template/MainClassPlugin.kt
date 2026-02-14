package org.winlogon.template

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

import org.bukkit.plugin.java.JavaPlugin
import org.winlogon.retrohue.RetroHue

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.Command

class MainClassPlugin : JavaPlugin() {
    private lateinit var miniMessage: MiniMessage
    private lateinit var formatter: RetroHue

    override fun onLoad() {
        miniMessage = MiniMessage.miniMessage()
        formatter = RetroHue(miniMessage)
    }

    override fun onEnable() {
        saveDefaultConfig()
        registerCommands()
    }

    private fun registerCommands() {
        this.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            val sayCommand = Commands.literal("say")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes { ctx ->
                        val message = StringArgumentType.getString(ctx, "message")
                        val component = parseMessage(message)
                        server.broadcast(component)
                        Command.SINGLE_SUCCESS
                    }
                )
                .build()
            commands.registrar().register(sayCommand)
        }
    }

    /**
     * Converts legacy ampersand codes to MiniMessage tags and deserializes the string into a Component.
     *
     * This function supports both legacy codes (e.g. &a, &c) and native MiniMessage tags.
     */
    private fun parseMessage(input: String): Component {
        return formatter.convertToComponent(input, '&')
    }
}
