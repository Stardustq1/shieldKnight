package com.example.shieldknight

import android.graphics.Bitmap

class Animation(private val spriteSheet: Bitmap, private val frameCount: Int) {
    private val frameWidth = spriteSheet.width / frameCount
    private val frameHeight = spriteSheet.height
    private var currentFrame = 0
    private var frameTime = 0
    private val frameDelay = 5  // Скорость анимации

    fun update() {
        frameTime++
        if (frameTime >= frameDelay) {
            currentFrame = (currentFrame + 1) % frameCount
            frameTime = 0
        }
    }

    fun getCurrentFrame(): Bitmap {
        return Bitmap.createBitmap(spriteSheet, currentFrame * frameWidth, 0, frameWidth, frameHeight)
    }
}