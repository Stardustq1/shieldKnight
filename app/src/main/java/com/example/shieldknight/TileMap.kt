package com.example.shieldknight

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas

class TileMap(private val tileSet: Tile, val mapData: List<List<Int>>, private val tileSize: Int, private val padding: Int, private val screenHeight: Int) {
    fun draw(canvas: Canvas) {
        val totalRows = mapData.size // Количество строк в карте

        for (row in mapData.indices) {
            for (col in mapData[row].indices) {
                val tileType = mapData[row][col]
                if (tileType != -1) { // -1 означает пустую клетку
                    val x = col * (tileSize + padding).toFloat()
                    val y = (screenHeight - (totalRows - row) * (tileSize + padding+10)).toFloat()
                    val bitmap = tileSet.tiles.getOrNull(tileType)
                    bitmap?.let { canvas.drawBitmap(it, x, y, null) }
                }
            }
        }
    }
}