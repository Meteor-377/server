package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.message.impl.NpcActionMessage
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.dialogue.DialogueResponse
import org.apollo.plugins.location.tutorial.TutorialPlugin.Companion.getTutorialProgress

class Chat0(val npcActionMessage: NpcActionMessage, npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        if (player.getTutorialProgress() == 1L)
            Chat1b(npc).send(player)
        else
            ChatNpcAction.start(npcActionMessage, player, npc.position) {
                player.sendNpc2Dialogue(
                    npc, ChatEmotes.DEFAULT,
                    "Greetings! I see you are a new arrival to this land. My",
                    "job is to welcome all the new visitors So welcome!", this
                )
            }
    }

    override fun continued(player: Player) {
        Chat1a(npc).send(player)
    }
}