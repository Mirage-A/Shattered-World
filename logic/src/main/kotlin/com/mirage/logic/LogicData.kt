package com.mirage.logic

import com.mirage.core.datastructures.Point
import com.mirage.core.extensions.*
import com.mirage.core.game.maps.GameMap
import com.mirage.core.game.maps.SceneLoader
import com.mirage.core.game.maps.ScriptArea
import com.mirage.core.game.states.ExtendedState
import com.mirage.core.game.states.SimplifiedState
import com.mirage.core.messaging.ClientMessage
import com.mirage.core.messaging.ServerMessage
import com.mirage.logic.behavior.Behavior
import org.luaj.vm2.LuaTable
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * State of a single game logic instance
 */
internal data class LogicData(
        val gameMapName: GameMapName,

        val serverMessages: Queue<Pair<EntityID, ServerMessage>> = ConcurrentLinkedQueue(),
        val playerTransfers: Queue<PlayerTransferRequest> = ConcurrentLinkedQueue(),

        /** Queue which stores new messages from clients */
        val clientMessages : Queue<Pair<EntityID, ClientMessage>> = ConcurrentLinkedQueue(),

        val sceneLoader: ExtendedSceneLoader = ExtendedSceneLoader(gameMapName),

        val gameMap : GameMap = sceneLoader.loadMap(),
        val state: ExtendedState = sceneLoader.loadInitialState(),
        val scriptAreas: Iterable<ScriptArea> = sceneLoader.loadAreas(),
        var latestStateSnapshot: SimplifiedState = state.simplifiedDeepCopy(),

        val behaviors: MutableMap<EntityID, Behavior> =
                state.entities.mapValues { sceneLoader.loadBehavior(it.value.template) }.toMutableMap(),

        /** These maps must be mutated only through ScriptActions class */
        val playerGlobalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),
        val playerLocalQuestProgress: MutableMap<EntityID, QuestProgress> = HashMap(),

        /** Set of IDs of all players */
        val playerIDs: MutableSet<EntityID> = HashSet(),

        /**
         * Queue which stores new requests for new player creation.
         * After a player entity was created by logic loop, PlayerCreationListener is invoked.
         */
        val newPlayerRequests : Queue<PlayerCreationRequest> = ConcurrentLinkedQueue(),

        /** Queue which stores new requests for player removing. */
        val removePlayerRequests: Queue<EntityID> = ConcurrentLinkedQueue(),

        var initScriptInvoked: Boolean = false,

        /** Latest entity positions processed by [processScriptAreas] */
        var lastProcessedPositions: MutableMap<EntityID, Point> = HashMap(),

        /** Requests to invoke script at given time */
        val delayedScripts: PriorityQueue<Pair<TimeMillis, Pair<String, LuaTable>>> = PriorityQueue(compareBy {it.first} )

)