package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.message.impl.HintIconMessage
import org.apollo.game.message.impl.PositionHintIconMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.dialogue.DialogueResponse
import org.apollo.plugins.location.tutorial.TutorialPlugin
import org.apollo.plugins.location.tutorial.TutorialPlugin.Companion.setTutorialProgress

class Chat6(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpc2Dialogue(
                npc, ChatEmotes.DEFAULT,
                "To continue the tutorial go through that door over",
                "there and speak to your first instructor!",
                this
            )
        }
    }

    override fun continued(player: Player) {
        player.setTutorialProgress(3L)
        player.send(PositionHintIconMessage(HintIconMessage.Type.EAST, Position(3097, 3107), 120))
        TutorialPlugin.sendTutorialInfo(
            player,
            "@blu@Interacting with scenery",
            "You can interact with many items of scenery by simply clicking",
            "on them. Right clicking will also give more options. Feel free to",
            "try it with the things in this room, then click on the door",
            "indicated with the yellow arrow to go through."
        )
    }
}