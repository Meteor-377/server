package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.dialogue.DialogueResponse
import org.apollo.plugins.location.tutorial.TutorialPlugin.Companion.getTutorialProgress

class Chat2(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpc3Dialogue(
                npc, ChatEmotes.DEFAULT,
                "You will find many inhabitants of this world have useful",
                "things to say to you. By clicking on them with your",
                "mouse you can talk to them.",
                this
            )
        }
    }

    override fun continued(player: Player) {
        if (player.getTutorialProgress() >= 1L)
            Chat4(npc).send(player)
        else
            Chat3(npc).send(player)
    }
}