package com.example.shieldknight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Tile(context: Context) {
    val tiles = listOf(
        BitmapFactory.decodeResource(context.resources, R.drawable.tile_grass)
            .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) },
        BitmapFactory.decodeResource(context.resources, R.drawable.tile_rock)
            .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) },
    )
}