package org.apollo.plugins.navigation.door.doors

import org.apollo.game.message.impl.ConfigMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.plugins.api.VarpID
import org.apollo.plugins.location.tutorial.TutorialPlugin
import org.apollo.plugins.navigation.door.RequirementDoor

object TutorialDoor1 : RequirementDoor(Position(3098, 3107)) {
    override fun meetsRequirement(player: Player): Boolean {
        return TutorialPlugin.getTutorialProgress(player) == 3L
    }

    override fun onAction(player: Player) {
        TutorialPlugin.setTutorialProgress(player, 4L)
        player.send(ConfigMessage(VarpID.TUTORIAL_PROGRESS_BAR_VARP, 3))
        TutorialPlugin.updateTabs(player, TutorialPlugin.TUTORIAL_STEP1_TABS)
        TutorialPlugin.sendMovingAround(player)
    }
}