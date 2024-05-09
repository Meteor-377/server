package org.apollo.plugins.location.tutorial.rsguide

import org.apollo.game.message.impl.MobHintIconMessage
import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.World
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.attr.AttributeDefinition
import org.apollo.game.model.entity.attr.AttributeMap
import org.apollo.game.model.entity.attr.AttributePersistence
import org.apollo.game.model.event.impl.LoginEvent
import org.apollo.game.plugin.KotlinPlugin
import org.apollo.game.plugin.PluginContext
import org.apollo.plugins.location.tutorial.TutorialPlugin.Companion.getTutorialProgress
import org.apollo.plugins.location.tutorial.TutorialPlugin.Companion.inTutorial
import org.apollo.plugins.location.tutorial.rsguide.chat.Chat0
import org.apollo.plugins.location.tutorial.rsguide.chat.Chat5

class RunescapeGuideNpcController(world: World, context: PluginContext) : KotlinPlugin(
    world, context,
    name = "Runescape Guide Controller", author = "Null"
) {

    init {
        AttributeMap.define("tutorial_progress", AttributeDefinition.forInt(0, AttributePersistence.PERSISTENT))
    }

    lateinit var npc: Npc

    override fun onNpcFirstAction() = { player: Player, npcActionMessage: NpcActionMessage ->
        player.sendMessage("Npc idx:${npcActionMessage.index} option:${npcActionMessage.option}")
        when (player.getTutorialProgress()) {
            0L, 1L -> Chat0(npcActionMessage, npc).send(player)
            2L -> Chat5(npc).send(player)
        }
    }

    override fun onLogin() = { event: LoginEvent ->
        npc = world.npcRepository.first { it.definition.name == "RuneScape Guide" }
        if (event.player.inTutorial()) {
            when (event.player.getTutorialProgress()) {
                0L, 1L -> event.player.send(MobHintIconMessage.create(npc))
            }
        }
    }
}