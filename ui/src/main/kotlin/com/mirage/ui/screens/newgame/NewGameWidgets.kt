package com.mirage.ui.screens.newgame

import com.mirage.core.VirtualScreen
import com.mirage.core.utils.ClientPlatform
import com.mirage.ui.widgets.*


private const val classBtnBorderSize = 4f
private const val classNameLabelFontSize = 32f

internal class NewGameWidgets(virtualScreen: VirtualScreen) {

    val classArt = ImageWidget(textureName = "", isVisible = false)

    val descriptionBackground = ImageWidget(textureName = "ui/new-game/description-background")

    val warriorBtn = Button(
            textureName = "ui/new-game/warrior-icon",
            highlightedTextureName = "ui/new-game/warrior-icon-highlighted",
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val rogueBtn = Button(
            textureName = "ui/new-game/rogue-icon",
            highlightedTextureName = "ui/new-game/rogue-icon-highlighted",
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val mageBtn = Button(
            textureName = "ui/new-game/mage-icon",
            highlightedTextureName = "ui/new-game/mage-icon-highlighted",
            borderTextureName = "ui/new-game/description-background",
            borderSize = classBtnBorderSize
    )

    val classNameLabel = LabelWidget(virtualScreen, "ASSASSINATION", classNameLabelFontSize, isVisible = false)

    val confirmBtn = Button(
            boundedLabel = LabelWidget(virtualScreen, "Confirm", 30f),
            isVisible = false
    )

    val descriptionLabel = LabelWidget(virtualScreen, "Choose starting specialization", 24f)

    val nameAreaBackground = ImageWidget(
            textureName = "ui/new-game/text-field-background",
            isVisible = ClientPlatform.platform != ClientPlatform.ANDROID
    )

    val profileNameField = TextFieldWidget(
            virtualScreen = virtualScreen,
            hint = "Enter your name",
            fontCapHeight = 20f,
            isVisible = ClientPlatform.platform != ClientPlatform.ANDROID
    )

    val rootWidget = CompositeWidget(
            profileNameField, nameAreaBackground, descriptionLabel, confirmBtn,
            classNameLabel, warriorBtn, rogueBtn, mageBtn, descriptionBackground, classArt
    )
}