package com.mirage.ui.mainmenu

import com.mirage.ui.widgets.Button
import com.mirage.utils.datastructures.Rectangle
import com.mirage.utils.virtualscreen.VirtualScreen

class MainMenuUIState(val virtualScreen: VirtualScreen) {

    private val btnWidth = 400f
    private val btnHeight = 80f


    val singlePlayerBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Campaign", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 7 / 2, btnWidth, btnHeight)})

    val multiPlayerBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Multiplayer", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 5 / 2, btnWidth, btnHeight)})

    val settingsBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Settings", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight * 3 / 2, btnWidth, btnHeight)})

    val exitBtn = Button("ui/main-menu-btn",
            "ui/main-menu-btn-highlighted",
            "ui/main-menu-btn-pressed",
            Rectangle(),
            virtualScreen.createLabel("Exit", 30f),
            {_, virtualHeight -> Rectangle(0f, - virtualHeight / 2 + btnHeight / 2, btnWidth, btnHeight)})

    val btnList: List<Button> = listOf(singlePlayerBtn, multiPlayerBtn, settingsBtn, exitBtn)


    fun resize(virtualWidth: Float, virtualHeight: Float) {
        for (btn in btnList) {
            btn.resize(virtualWidth, virtualHeight)
        }
    }

    init {
        resize(virtualScreen.width, virtualScreen.height)
    }
}