package org.apollo.plugins

import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.KotlinPlugin
import org.apollo.game.plugin.PluginContext
import org.apollo.plugins.api.ButtonID

class LogoutPlugin(world: World, context: PluginContext) : KotlinPlugin(
    world, context,
    name = "Logout", author = "Apollo"
) {
    override fun onButton() = { player: Player, event: ButtonMessage ->
        if (event.widgetId == ButtonID.LOGOUT)
            player.logout()
    }
}