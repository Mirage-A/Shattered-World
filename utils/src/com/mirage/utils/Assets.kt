package com.mirage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import java.io.File
import java.io.InputStream
import java.io.Reader
import kotlin.math.roundToInt

object Assets {

    val assetsPath = if ((PLATFORM == "test" || PLATFORM == "desktop-test") && File("./android/assets/").exists())
        "./android/assets/" else ""

    private val assetsResolver : FileHandleResolver = when (PLATFORM) {
        "test" -> FileHandleResolver {
            FileHandle(File(File("").absoluteFile.parentFile.absolutePath + "/android/assets/$it"))
        }
        "desktop-test" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "server" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "desktop", "android", "ios" -> FileHandleResolver {
            Gdx.files.internal(it)
        }
        else -> {
            throw Exception("Unknown platform: $PLATFORM")
        }
    }

    fun loadFile(path: String) : FileHandle? {
        val file = assetsResolver.resolve(path)
        return if (file == null) {
            Log.e("File not found: $path")
            null
        }
        else file
    }

    fun loadReader(path: String) =
            assetsResolver.resolve(path).reader()

    fun loadClientScript(name: String) : Reader? =
            loadReader("scripts/client/$name.lua")

    fun loadLogicScript(name: String) : Reader? =
            loadReader("scripts/logic/$name.lua")

    private val MIN_FILTER = Texture.TextureFilter.MipMapLinearNearest
    private val MAG_FILTER = Texture.TextureFilter.MipMapLinearNearest
    /**
     * Загружает текстуры из папки drawable по относительному пути path и применяет к ним заданные фильтры.
     */
    fun getRawTexture(name: String) : Texture {
        val file = assetsResolver.resolve("drawable/$name.png")
        file ?: return Texture(0, 0, Pixmap.Format.Alpha)
        val loadedTexture = Texture(file, true)
        loadedTexture.setFilter(MIN_FILTER, MAG_FILTER)
        return loadedTexture
    }

    fun loadAnimation(name: String) : InputStream? =
        loadFile("animations/$name.swa")?.read()

    fun loadTileTexturesList(name: String) : List<TextureRegion> {
        val fullTexture = getRawTexture("tiles/$name")
        val regions = TextureRegion.split(fullTexture, TILE_WIDTH.roundToInt(), TILE_HEIGHT.roundToInt())
        return regions.flatten()
    }


}