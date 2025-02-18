package com.example.shieldknight
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect

class Enemy(private val context: Context, var x: Float, var y: Float) {
    private val spriteSheet: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy_walk)
        .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }
    val frameWidth = spriteSheet.width / 8
    val frameHeight = spriteSheet.height


    private var currentFrame = 0
    private var isFacingRight = true
    private val speed = 2f // Скорость врага
    private var frameCounter = 0 // Счётчик для замедления анимации
    private val frameDelay = 5  //

    fun update(playerX: Float) {
        // Двигаемся к игроку
        if (x < playerX) {
            x += speed
            isFacingRight = true
        } else if (x > playerX) {
            x -= speed
            isFacingRight = false
        }

        // Меняем кадр анимации
        frameCounter++
        if(frameCounter>=frameDelay){
            frameCounter=0
            currentFrame = (currentFrame + 1) % 8
        }
    }

    fun draw(canvas: Canvas) {
        val srcRect = Rect(currentFrame * frameWidth, 0, (currentFrame + 1) * frameWidth, frameHeight)
        val dstRect = Rect(x.toInt(), y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt())

        val frameBitmap = Bitmap.createBitmap(spriteSheet, srcRect.left, srcRect.top, frameWidth, frameHeight)

        val flippedBitmap = if (!isFacingRight) {//Поворот битмапа персонажа
            val matrix = Matrix()
            matrix.preScale(-1f, 1f)
            Bitmap.createBitmap(frameBitmap, 0, 0, frameWidth, frameHeight, matrix, true)
        } else {
            frameBitmap
        }

        canvas.drawBitmap(flippedBitmap, null, dstRect, null)
    }
}