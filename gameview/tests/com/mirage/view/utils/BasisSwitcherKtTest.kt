package com.mirage.view.utils

import com.mirage.utils.Log
import com.mirage.utils.TILE_HEIGHT
import com.mirage.utils.TILE_WIDTH
import com.mirage.utils.datastructures.Point
import com.mirage.view.utils.getScenePointFromVirtualScreen
import com.mirage.view.utils.getVirtualScreenPointFromScene
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BasisSwitcherKtTest {

    @Test
    fun testBasisSwitching() {
        assertEquals(Point(0f, 0f),
                getVirtualScreenPointFromScene(Point(0f, 0f)))
        assertEquals(Point(0f, 0f), getScenePointFromVirtualScreen(getVirtualScreenPointFromScene(Point(0f, 0f))))
        assertEquals(Point(0f, 0f), getVirtualScreenPointFromScene(getScenePointFromVirtualScreen(Point(0f, 0f))))
        assertEquals(Point(TILE_WIDTH / 2, TILE_HEIGHT / 2), getVirtualScreenPointFromScene(Point(0f, 1f)))
        assertEquals(Point(TILE_WIDTH * 2, 0f), getVirtualScreenPointFromScene(Point(2f, 2f)))
    }

}