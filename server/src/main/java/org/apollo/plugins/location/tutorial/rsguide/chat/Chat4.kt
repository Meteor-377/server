package org.apollo.plugins.location.tutorial.rsguide.chat

import org.apollo.game.message.impl.HintIconMessage
import org.apollo.game.message.impl.MobHintIconMessage
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Npc
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.ChatEmotes
import org.apollo.plugins.api.dialogue.ChatNpcAction
import org.apollo.plugins.api.dialogue.DialogueResponse
import org.apollo.plugins.location.tutorial.TutorialPlugin

class Chat4(npc: Npc) : DialogueResponse(npc) {
    override fun send(player: Player) {
        ChatNpcAction.start(player, npc.position) {
            player.sendNpc2Dialogue(
                npc, ChatEmotes.DEFAULT,
                "You will notice a flashing icon of a wrench, please click",
                "on this to continue the tutorial.",
                this
            )
        }
    }

    override fun continued(player: Player) {
        player.send(MobHintIconMessage.reset(EntityType.NPC))
        TutorialPlugin.sendSettingsStep(player)
    }
}