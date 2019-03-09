package com.mirage.gamelogic

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mirage.configuration.Log
import com.mirage.scriptrunner.logic.LogicEventHandler
import com.mirage.utils.extensions.*
import kotlin.math.log

class GameLoop {
    var map = TiledMap()

    var player: MapObject? = null

    val logicEventHandler = LogicEventHandler(LogicScriptActionsImpl(this))

    /**
     * Этот параметр позволяет приостанавливать логику игры, а затем снова запускать
     */
    var isPaused = true

    /**
     * Перемещение персонажа, которое считается достаточно малым, чтобы при таком перемещении можно было рассматривать только соседние тайлы
     * Длинные перемещения разбиваются на малые такой длины
     */
    private val smallRange = 0.5f

    /**
     * Лимит кол-ва итераций цикла за секунду
     */
    private val ticksPerSecondLimit = 512

    var walkabilities = Array(0){IntArray(0)}

    /**
     * Тик игровой логики
     */
    fun update() {
        if (!isPaused) {
            if (Gdx.graphics.deltaTime > 0.1f) Log.i("Slow update: " + Gdx.graphics.deltaTime + " s")
            for (layer in map.layers) {
                for (obj in layer.objects) {
                    if (obj.name == "player") {
                        if (obj.isMoving) {
                            moveEntity(obj)
                        }
                    }
                }
            }
        }
    }


    /**
     * Обрабатывает передвижение данного объекта за тик
     */
    private fun moveEntity(obj: MapObject) {
        val range = obj.properties.getFloat("speed", 0f) * Time.deltaTime
        for (i in 0 until (range / smallRange).toInt()) {
            smallMoveEntity(obj, smallRange)
        }
        smallMoveEntity(obj, range % smallRange)
    }

    /**
     * Отступ от границы непроходимого тайла
     */
    private val eps = 0.000001f

    /**
     * Обрабатывает короткое (на расстояние не более smallRange) передвижение данного объекта
     * Для обычного передвижения следует использовать moveEntity
     */
    private fun smallMoveEntity(obj: MapObject, range: Float) {
        val rect = obj.rectangle
        val oldPosition = obj.position
        val newPosition = obj.position
        newPosition.move(obj.moveAngle, range)
        newPosition.x = Math.max(eps, Math.min(map.properties.getInt("width", 0) - eps - rect.width, newPosition.x))
        newPosition.y = Math.max(eps, Math.min(map.properties.getInt("height", 0) - eps - rect.height, newPosition.y))
        val newRect = Rectangle(newPosition.x, newPosition.y, rect.width, rect.height)
        //TODO Пересечения объектов
        if (obj.isRigid) {
            for (otherObj in map) {
                if (otherObj != obj && otherObj.rectangle.overlaps(newRect) && otherObj.isRigid) return
            }
        }
        for (point in newRect.points) {
            if (!isTileWalkable(point.x.toInt(), point.y.toInt())) {
                return
            }
        }
        obj.position = newPosition
        logicEventHandler.handleObjectMove(obj, oldPosition, newPosition)
    }

    /**
     * Найти игрока среди объектов
     */
    fun findPlayer() {
        player = map.findObject("player")
    }

    fun isTileWalkable(x: Int, y: Int) : Boolean {
         return getTileId(x, y) == 1
    }

    fun isTileShootable(x: Int, y: Int) : Boolean {
        val id = getTileId(x, y)
        return id == 1 || id == 2
    }

    fun getTileId(x: Int, y: Int) : Int {
        return walkabilities[x][y]
    }

    fun setTileId(x: Int, y: Int, id: Int) {
        walkabilities[x][y] = id
    }
}