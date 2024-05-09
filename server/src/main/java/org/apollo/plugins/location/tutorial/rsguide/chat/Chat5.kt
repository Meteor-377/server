package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.attr.NumericalAttribute
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.DialogueResponse
import org.apollo.plugins.location.tutorial.TutorialPlugin

class Chat5(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpcDialogue(npc, ChatEmotes.DEFAULT,
                "I'm glad you're making progress!",
                this)
        }
    }

    override fun continued(player: Player) {
        Chat6(npc).send(player)
    }
}