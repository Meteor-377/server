package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.dialogue.DialogueResponse

class Chat1b(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpc2Dialogue(
                npc, ChatEmotes.DEFAULT,
                "Welcome back. You have already learned the first thing",
                "needed to succeed in this world: talking to other people!", this
            )
        }
    }

    override fun continued(player: Player) {
        Chat2(npc).send(player)
    }
}