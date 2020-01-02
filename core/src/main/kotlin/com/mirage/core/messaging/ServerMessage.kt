package com.mirage.core.messaging

import com.mirage.core.game.objects.properties.Equipment
import com.mirage.core.game.objects.SimplifiedState
import com.mirage.core.game.objects.StateDifference
import com.mirage.core.utils.BuildingID
import com.mirage.core.utils.EntityID

sealed class ServerMessage {
    companion object {

        /** List of all classes inherited from [ServerMessage] */
        private val serverMessageClasses = listOf<Class<*>>(
                InitialGameStateMessage::class.java,
                GameStateUpdateMessage::class.java,
                GameOverMessage::class.java,
                ReturnCodeMessage::class.java,
                HumanoidEquipmentUpdateMessage::class.java,
                GlobalQuestUpdateMessage::class.java,
                LocalQuestUpdateMessage::class.java,
                DisplayTextMessage::class.java,
                StartDialogMessage::class.java,
                EntityTeleportMessage::class.java,
                BuildingTeleportMessage::class.java
        )

        internal val codeToClassMap: Map<Int, Class<*>> = HashMap<Int, Class<*>>().apply {
            for ((index, value) in serverMessageClasses.withIndex()) {
                this[index] = value
            }
        }

        internal val classToCodeMap: Map<Class<*>, Int> = HashMap<Class<*>, Int>().apply {
            for ((index, value) in serverMessageClasses.withIndex()) {
                this[value] = index
            }
        }
    }
}


/**
 * Message with full information about game state.
 * This message is sent only when a new client is connected to game logic.
 * [stateCreatedTimeMillis] - time of [initialState] creation.
 */
data class InitialGameStateMessage(
        val mapName: String,
        val initialState: SimplifiedState,
        val playerID: Long,
        val stateCreatedTimeMillis: Long
) : ServerMessage()

/** This message is sent by logic after each tick of game loop */
data class GameStateUpdateMessage(
        val diff: StateDifference,
        val stateCreatedTimeMillis: Long
) : ServerMessage()

/** This message in sent to all players when all players are dead and game must be fully restarted */
data class GameOverMessage(
        val message: String? = null
) : ServerMessage()

data class ReturnCodeMessage(
        val returnCode: Int
) : ServerMessage()

data class HumanoidEquipmentUpdateMessage(
        val objectID: Long,
        val newEquipment: Equipment
) : ServerMessage()

data class GlobalQuestUpdateMessage(
        val globalQuestName: String,
        val newPhaseID: Int
) : ServerMessage()

data class LocalQuestUpdateMessage(
        val localQuestName: String,
        val newPhaseID: Int
) : ServerMessage()

data class DisplayTextMessage(
        val text: String
) : ServerMessage()

data class StartDialogMessage(
        val dialogName: String
) : ServerMessage()

/** Marks [entityID] as teleported entity, meaning that client will not interpolate its movement during current tick */
data class EntityTeleportMessage(
        val entityID: EntityID
) : ServerMessage()

/** Marks [buildingID] as teleported building, meaning that client will not interpolate its movement during current tick */
data class BuildingTeleportMessage(
        val buildingID: BuildingID
) : ServerMessage()