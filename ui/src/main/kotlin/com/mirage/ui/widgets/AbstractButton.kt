package com.mirage.ui.widgets

internal interface AbstractButton : Widget {
    var textureName: String
    var highlightedTextureName: String
    var pressedTextureName: String
    var boundedLabel: LabelWidget?
}