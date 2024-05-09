package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.DialogueResponse

class Chat3(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpc3Dialogue(npc, ChatEmotes.DEFAULT,
                "I would also suggest reading through some of the",
                "supporting information on the website. There you can",
                "find maps, a bestiary, and much more.",
                this)
        }
    }

    override fun continued(player: Player) {
        Chat4(npc).send(player)
    }
}