package org.apollo.plugins.location.tutorial

import org.apollo.game.message.impl.*
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.attr.Attribute
import org.apollo.game.model.entity.attr.NumericalAttribute
import org.apollo.game.model.event.impl.LoginEvent
import org.apollo.game.plugin.KotlinPlugin
import org.apollo.game.plugin.PluginContext
import org.apollo.plugins.api.ComID.TUTORIAL_INFO
import org.apollo.plugins.api.ComID.TUTORIAL_INFO_LINE1
import org.apollo.plugins.api.ComID.TUTORIAL_INFO_LINE2
import org.apollo.plugins.api.ComID.TUTORIAL_INFO_LINE3
import org.apollo.plugins.api.ComID.TUTORIAL_INFO_LINE4
import org.apollo.plugins.api.ComID.TUTORIAL_INFO_TITLE
import org.apollo.plugins.api.ComID.TUTORIAL_PROGRESS_ARROW_LAYER
import org.apollo.plugins.api.ComID.TUTORIAL_PROGRESS_ROOT
import org.apollo.plugins.api.TabID
import org.apollo.plugins.api.VarpID
import org.apollo.plugins.location.tutorial.rsguide.RunescapeGuideNpcController

class TutorialPlugin(world: World, context: PluginContext) : KotlinPlugin(
    world, context,
    name = "Tutorial", author = "Null"
) {

    override fun onLogin() = { event: LoginEvent ->
        if (event.player.inTutorial()) {
            event.player.send(PlayMidiMessage(62, 0))
            //Prevent closing dialogue when walking/interacting
            //Players should not be able to talk until completing tutorial
            event.player.interfaceSet.canClose = false
            event.player.interfaceSet.openOverlay(TUTORIAL_PROGRESS_ROOT)
            //TODO: should show if player taking too long
            //This layer is shown by default, and must be hidden
            event.player.send(SetWidgetVisibilityMessage(TUTORIAL_PROGRESS_ARROW_LAYER, false))
            //Define tutorial_progress so we can access it without worrying about nullability
            event.player.attributes.putIfAbsent("tutorial_progress", NumericalAttribute(0L))
            //Send tutorial state
            when (event.player.getTutorialProgress()) {
                0L -> {
                    event.player.send(ConfigMessage(VarpID.TUTORIAL_PROGRESS_BAR_VARP, 0))
                    updateTabs(event.player, TUTORIAL_STEP0_TABS)
                    sendGettingStarted(event.player)
                }

                1L, 2L -> {
                    event.player.send(ConfigMessage(VarpID.TUTORIAL_PROGRESS_BAR_VARP, 0))
                    updateTabs(event.player, TUTORIAL_STEP1_TABS)
                    sendSettingsStep(event.player)
                }

                3L -> {
                    event.player.send(ConfigMessage(VarpID.TUTORIAL_PROGRESS_BAR_VARP, 0))
                    updateTabs(event.player, TUTORIAL_STEP1_TABS)
                    sendInteractingWithScenery(event.player)
                }

                4L -> {
                    event.player.send(ConfigMessage(VarpID.TUTORIAL_PROGRESS_BAR_VARP, 3))
                    updateTabs(event.player, TUTORIAL_STEP1_TABS)
                    sendMovingAround(event.player)
                }
            }
        }
        println("position : ${event.player.position}")
    }

    override fun onFlashingTabClicked() = { player: Player, event: FlashingTabClickedMessage ->
        if (event.tab == TabID.SETTINGS) {
            player.send(MobHintIconMessage.create(RunescapeGuideNpcController.npc))
            sendPlayerControls(player)
            player.setTutorialProgress(2L)
        }
    }

    override fun onWalkAction() = { player: Player, _: WalkMessage ->
        if (!player.interfaceSet.contains(TUTORIAL_INFO)) {
            when (player.getTutorialProgress()) {
                0L -> sendGettingStarted(player)
                1L, 2L -> sendPlayerControls(player)
                3L -> sendInteractingWithScenery(player)
                4L -> sendMovingAround(player)
            }
        }
    }

    fun sendInteractingWithScenery(player: Player) {
        player.send(PositionHintIconMessage(HintIconMessage.Type.EAST, Position(3097, 3107), 120))
        sendTutorialInfo(
            player,
            "@blu@Interacting with scenery",
            "You can interact with many items of scenery by simply clicking",
            "on them. Right clicking will also give more options. Feel free to",
            "try it with the things in this room, then click on the door",
            "indicated with the yellow arrow to go through."
        )
    }

    fun sendGettingStarted(player: Player) {
        sendTutorialInfo(
            player,
            "@blu@Getting started",
            "To start the tutorial use your left mouse-button to click on the",
            "'Runescape Guide' in this room. He is indicated by a flashing",
            "yellow arrow above his head. If you can't see him, use your",
            "keyboard's arrow keys to rotate the view."
        )
    }

    fun sendPlayerControls(player: Player) {
        sendTutorialInfo(
            player,
            "@blu@Player controls",
            "On the side panel, you can now see a variety of options from",
            "changing the brightness of the screen and the volume of",
            "music, to selecting whether your player should accept help",
            "from other players. Don't worry about these too much for now."
        )
    }

    companion object {
        fun sendMovingAround(player: Player) {
            sendTutorialInfo(
                player,
                "@blu@Moving around",
                "Follow the path to find the next instructor. Clicking on the",
                "ground will walk you to that point. You can also move around by",
                "clicking a point on the minimap in the top right corner. Talk to",
                "the survival expert to continue the tutorial."
            )
        }

        fun sendTutorialInfo(
            player: Player, title: String,
            text: String, text1: String, text2: String,
            text3: String,
        ) {
            player.send(SetWidgetTextMessage(TUTORIAL_INFO_TITLE, title))
            player.send(SetWidgetTextMessage(TUTORIAL_INFO_LINE1, text))
            player.send(SetWidgetTextMessage(TUTORIAL_INFO_LINE2, text1))
            player.send(SetWidgetTextMessage(TUTORIAL_INFO_LINE3, text2))
            player.send(SetWidgetTextMessage(TUTORIAL_INFO_LINE4, text3))
            player.interfaceSet.openDialogue(TUTORIAL_INFO)
        }

        fun sendSettingsStep(player: Player) {
            sendTutorialInfo(
                player,
                "@blu@Player controls",
                "Please click on the flashing wrench icon found at the bottom",
                "right of your screen. This will display your player controls.",
                "",
                ""
            )
            player.setTutorialProgress(1L)
            updateTabs(player, TUTORIAL_STEP1_TABS)
            player.send(FlashTabInterfaceMessage(11))
        }

        fun updateTabs(player: Player, tabs: IntArray) {
            for (tab in tabs.indices) {
                player.send(SwitchTabInterfaceMessage(tab, tabs[tab]))
            }
        }

        fun Player.getTutorialProgress(): Long {
            val tutorialProgress: Attribute<*> = getAttribute("tutorial_progress")
            return tutorialProgress.value as Long
        }

        fun Player.setTutorialProgress(progress: Long) {
            setAttribute("tutorial_progress", NumericalAttribute(progress))
        }


        //TODO: Use actual bounds
        fun Player.inTutorial(): Boolean {
            if (position.x in 3000..3200)
                if (position.y in 3000..3200)
                    return true
            return false
        }

        /**
         * The default inventory tab ids.
         */
        val DEFAULT_INVENTORY_TABS: IntArray = intArrayOf(
            2423, 3917, 638, 3213, 1644, 5608, 1151, -1,
            5065, 5715, 2449,
            904, 147, 962,
        )

        val TUTORIAL_STEP0_TABS: IntArray = intArrayOf(
            -1, -1, -1, -1, -1, -1, -1, 18128,
            -1, -1, 2449,
            -1, -1, -1,
        )
        val TUTORIAL_STEP1_TABS: IntArray = intArrayOf(
            -1, -1, -1, -1, -1, -1, -1, 18128,
            -1, -1, 2449,
            904, -1, -1,
        )
        val TUTORIAL_STEP2_TABS: IntArray = intArrayOf(
            -1, -1, -1, 3213, -1, -1, -1, 18128,
            -1, -1, 2449,
            904, -1, -1,
        )
    }
}