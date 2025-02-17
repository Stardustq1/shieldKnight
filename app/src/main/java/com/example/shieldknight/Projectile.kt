package com.example.shieldknight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF

class Projectile(var x: Float, var y: Float, private val direction: Int, private val context: Context) {
    private val speed = 10f
    private val sprite: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.wind_bolt)
        .let { Bitmap.createScaledBitmap(it, (it.width * 3f).toInt(), (it.height * 3f).toInt(), false) }
    private val width = sprite.width / 6
    private val height=sprite.height
    private var currentframe=0
    private var frametime=0

    fun update(): Boolean {
        x += speed * direction
        frametime++
        if (frametime>=5) {
            frametime=0
            currentframe = (currentframe + 1) % 6
        }
        return x > 2000 || x < -width // Уничтожаем снаряд, если он вышел за границы экрана
    }

    fun draw(canvas: Canvas) {
        val srcRect = Rect(currentframe * width, 0, (currentframe + 1) *  width,  height)
        val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
        val frameBitmap = Bitmap.createBitmap(sprite, srcRect.left, srcRect.top,  width,  height)
        val flippedBitmap = if (direction!=1) {
            val matrix = Matrix()
            matrix.preScale(-1f, 1f)
            Bitmap.createBitmap(frameBitmap, 0, 0, width, height, matrix, true)
        } else {
            frameBitmap
        }
        Bitmap.createBitmap(sprite, srcRect.left, srcRect.top, width, height)
        canvas.drawBitmap(flippedBitmap, null, dstRect, null)
    }

    fun checkCollision(enemy: Enemy): Boolean {
        val projectileRect = RectF(x, y, x + width, y + height)
        val enemyRect =
            RectF(enemy.x, enemy.y, enemy.x + enemy.frameWidth, enemy.y + enemy.frameHeight)
        return RectF.intersects(projectileRect, enemyRect)
    }
}