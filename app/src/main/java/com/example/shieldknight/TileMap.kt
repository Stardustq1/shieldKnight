package com.example.shieldknight

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas

class TileMap(context: Context, val tileSize: Int, val mapData: List<List<Int>>) {
    private val tileSprites = listOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.tile_grass),
        BitmapFactory.decodeResource(context.resources, R.drawable.tile_rock)
    )

    fun draw(canvas: Canvas) {
        for (row in mapData.indices) {
            for (col in mapData[row].indices) {
                val tileType = mapData[row][col]
                if (tileType != -1) {
                    val sprite = tileSprites[tileType]
                    val x = col * tileSize.toFloat()
                    val y = row * tileSize.toFloat()
                    canvas.drawBitmap(sprite, x, y, null)
                }
            }
        }
    }
}