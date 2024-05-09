package org.apollo.plugins.debug

import org.apollo.game.message.impl.WalkMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.KotlinPlugin
import org.apollo.game.plugin.PluginContext

class WalkLocationDebug(world: World, context: PluginContext) : KotlinPlugin(
    world, context,
    name = "Walk Location Debug", author = "Null"
) {

    override fun onWalkAction() = { player: Player, event: WalkMessage ->
        player.sendMessage("x: ${event.steps[0].x} y: ${event.steps[0].y}")
    }
}