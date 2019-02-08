package com.mirage.controller

import com.badlogic.gdx.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mirage.model.Model
import com.mirage.model.scripts.ScriptLoader
import com.mirage.view.LoadingView
import com.mirage.view.SceneView
import com.mirage.view.View
import com.mirage.view.animation.MoveDirection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.badlogic.gdx.utils.Align.center
import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.Gdx.files
import com.mirage.view.Log


object Controller : ApplicationAdapter(), InputProcessor {

    /**
     * Текущий экран
     */
    private var view : View? = null
    private var screenType = Screen.LOADING

    /**
     * Время отпускания клавиши передвижения
     */
    private var wReleasedTime = 0L
    private var aReleasedTime = 0L
    private var sReleasedTime = 0L
    private var dReleasedTime = 0L
    /**
     * Интервал времени, за который должны быть отпущены две соседние клавиши передвижения,
     * чтобы персонаж остался в диагональном направлении движения
     */
    private const val EPS_TIME = 50L

    override fun create() {
        val loadingView = LoadingView()
        view = loadingView
        val sceneView = SceneView()
        Gdx.input.inputProcessor = this
        GlobalScope.launch {
            ScriptLoader.load("scripts/mazewin.py")
            Model.startGame()
            Model.startLogic()
            view = sceneView
        }
        /*Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("android/assets/drop.wav"));
        Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("android/assets/rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();*/
    }

    private fun writeFile(fileName: String, text: String) {
        val file = Gdx.files.local("$fileName.txt")
        file.writeString(text, false)
    }

    override fun render() {
        // val deltaTime = Gdx.graphics.deltaTime
        // long time = TimeUtils.nanoTime()
        // rectangle.overlaps(rectangle) - коллизии
        // process user input
        /*
        if (Gdx.input.isTouched) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            View.camera.unproject(touchPos) //Этот метод переводит координаты на экране в координаты игры
            // (без учета всяких передвижений камеры). Нам этот метод не нужен, т.к. мы обрабатываем UI
        }
        */

        view?.render()
    }

    override fun dispose() {
        view?.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.LEFT -> {
                            Model.startMoving(MoveDirection.UP_LEFT.toAngle())
                        }
                        MoveDirection.RIGHT -> {
                            Model.startMoving(MoveDirection.UP_RIGHT.toAngle())
                        }
                        else -> {
                            Model.startMoving(MoveDirection.UP.toAngle())
                        }
                    }
                } else {
                    Model.startMoving(MoveDirection.UP.toAngle())
                }
            }
            Input.Keys.A -> {
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.UP -> {
                            Model.startMoving(MoveDirection.UP_LEFT.toAngle())
                        }
                        MoveDirection.DOWN -> {
                            Model.startMoving(MoveDirection.DOWN_LEFT.toAngle())
                        }
                        else -> {
                            Model.startMoving(MoveDirection.LEFT.toAngle())
                        }
                    }
                } else {
                    Model.startMoving(MoveDirection.LEFT.toAngle())
                }
            }
            Input.Keys.S -> {
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.LEFT -> {
                            Model.startMoving(MoveDirection.DOWN_LEFT.toAngle())
                        }
                        MoveDirection.RIGHT -> {
                            Model.startMoving(MoveDirection.DOWN_RIGHT.toAngle())
                        }
                        else -> {
                            Model.startMoving(MoveDirection.DOWN.toAngle())
                        }
                    }
                } else {
                    Model.startMoving(MoveDirection.DOWN.toAngle())
                }
            }
            Input.Keys.D -> {
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.UP -> {
                            Model.startMoving(MoveDirection.UP_RIGHT.toAngle())
                        }
                        MoveDirection.DOWN -> {
                            Model.startMoving(MoveDirection.DOWN_RIGHT.toAngle())
                        }
                        else -> {
                            Model.startMoving(MoveDirection.RIGHT.toAngle())
                        }
                    }
                } else {
                    Model.startMoving(MoveDirection.RIGHT.toAngle())
                }
            }
            Input.Keys.ESCAPE -> {
                //TODO Выход из игры
                Gdx.app.exit()
            }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> {
                wReleasedTime = System.currentTimeMillis()
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.UP_LEFT -> {
                            Model.startMoving(MoveDirection.LEFT.toAngle())
                        }
                        MoveDirection.UP_RIGHT -> {
                            Model.startMoving(MoveDirection.RIGHT.toAngle())
                        }
                        MoveDirection.UP -> {
                            if (aReleasedTime >= dReleasedTime && wReleasedTime - aReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.UP_LEFT.toAngle())
                            } else if (dReleasedTime >= aReleasedTime && wReleasedTime - dReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.UP_RIGHT.toAngle())
                            }
                            Model.stopMoving()
                        }
                    }
                }
            }
            Input.Keys.A -> {
                aReleasedTime = System.currentTimeMillis()
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.UP_LEFT -> {
                            Model.startMoving(MoveDirection.UP.toAngle())
                        }
                        MoveDirection.DOWN_LEFT -> {
                            Model.startMoving(MoveDirection.DOWN.toAngle())
                        }
                        MoveDirection.LEFT -> {
                            if (wReleasedTime >= sReleasedTime && aReleasedTime - wReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.UP_LEFT.toAngle())
                            } else if (sReleasedTime >= wReleasedTime && aReleasedTime - sReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.DOWN_LEFT.toAngle())
                            }
                            Model.stopMoving()
                        }
                    }
                }
            }
            Input.Keys.S -> {
                sReleasedTime = System.currentTimeMillis()
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.DOWN_LEFT -> {
                            Model.startMoving(MoveDirection.LEFT.toAngle())
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            Model.startMoving(MoveDirection.RIGHT.toAngle())
                        }
                        MoveDirection.DOWN -> {
                            if (aReleasedTime >= dReleasedTime && sReleasedTime - aReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.DOWN_LEFT.toAngle())
                            } else if (dReleasedTime >= aReleasedTime && sReleasedTime - dReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.DOWN_RIGHT.toAngle())
                            }
                            Model.stopMoving()
                        }
                    }
                }
            }
            Input.Keys.D -> {
                dReleasedTime = System.currentTimeMillis()
                if (Model.isPlayerMoving()) {
                    when (Model.getPlayerMoveDirection()) {
                        MoveDirection.UP_RIGHT -> {
                            Model.startMoving(MoveDirection.UP.toAngle())
                        }
                        MoveDirection.DOWN_RIGHT -> {
                            Model.startMoving(MoveDirection.DOWN.toAngle())
                        }
                        MoveDirection.RIGHT -> {
                            if (wReleasedTime >= sReleasedTime && dReleasedTime - wReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.UP_RIGHT.toAngle())
                            } else if (sReleasedTime >= wReleasedTime && dReleasedTime - sReleasedTime < EPS_TIME) {
                                Model.setMoveAngle(MoveDirection.DOWN_RIGHT.toAngle())
                            }
                            Model.stopMoving()
                        }
                    }
                }
            }
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }
}