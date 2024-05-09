package org.apollo.plugins.spawn

import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
import org.apollo.game.plugin.KotlinPlugin
import org.apollo.game.plugin.PluginContext
import org.apollo.game.plugin.api.Definitions

class SpawnPlugin(world: World, context: PluginContext) : KotlinPlugin(
    world, context,
    name = "Spawn", author = "Apollo"
) {
    override fun start() = { world: World ->
        for ((id, name, position, facing, animation, graphic) in Spawns.list) {
            val definition = requireNotNull(id?.let(Definitions::npc) ?: Definitions.npc(name)) {
                "Could not find an Npc named $name to spawn."
            }

            val npc = Npc(world, definition.id, position).apply {
                turnTo(position.step(1, facing))
                animation?.let(::playAnimation)
                graphic?.let(::playGraphic)
            }

            world.register(npc)
        }
    }
}