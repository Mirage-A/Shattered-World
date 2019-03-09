package com.mirage.assetmanager

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.mirage.configuration.Log
import com.mirage.configuration.config
import java.io.File
import java.io.InputStream
import java.io.Reader

object Assets {

    val assetsPath = if (config["test"] == true && File("./android/assets/").exists())
                                "./android/assets/" else ""

    fun loadFile(path: String) : FileHandle? {
        val file = Gdx.files.internal(assetsPath + path)
        return if (file == null) {
            Log.e("File not found: ${assetsPath + path}")
            null
        }
        else file
    }

    fun loadReader(path: String) : Reader? =
            Gdx.files.internal(assetsPath + path).reader()

    fun loadClientScript(name: String) : Reader? =
            loadReader("${assetsPath}scripts/client/$name.lua")

    fun loadLogicScript(name: String) : Reader? =
            loadReader("${assetsPath}scripts/logic/$name.lua")

    private val MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest
    private val MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest
    /**
     * Загружает текстуры из папки drawable по относительному пути path и применяет к ним заданные фильтры.
     */
    fun getRawTexture(name: String) : Texture {
        val file = Gdx.files.internal("${assetsPath}drawable/$name.png")
        file ?: return Texture(0, 0, Pixmap.Format.Alpha)
        val loadedTexture = Texture(file, true)
        loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
        return loadedTexture
    }

    fun loadAnimation(name: String) : InputStream? =
        loadFile("${assetsPath}animations/$name.swa")?.read()

}