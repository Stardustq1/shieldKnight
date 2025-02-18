package com.example.shieldknight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint

class Player(context: Context, var x: Float, var y: Float) {
    private val idle: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.hero)
        .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }

    //Создание переменных с битмап изображениями и увеличиваем размер в 2 раза
        private val idleSheet = BitmapFactory.decodeResource(context.resources, R.drawable.hero)
        .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }
        private val runSheet = BitmapFactory.decodeResource(context.resources, R.drawable.run)
            .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }
        private val attackSheet = BitmapFactory.decodeResource(context.resources, R.drawable.attack)
            .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }
        private val jumpSheet = BitmapFactory.decodeResource(context.resources, R.drawable.hero)
            .let { Bitmap.createScaledBitmap(it, (it.width * 2f).toInt(), (it.height * 2f).toInt(), false) }

    //Создание объектов класса анимация
        private val idleAnimation = Animation(idleSheet, 14)
        private val runAnimation = Animation(runSheet, 14)
        private val attackAnimation = Animation(attackSheet, 14)
        private val jumpAnimation = Animation(jumpSheet, 14)

        var isOnGround=true
        public var velocityY = 0f // Скорость для прыжка
        private val gravity = 2f // Гравитация
        private var currentAnimation=idleAnimation//Выбираем текущую анимацию

    private val projectiles = mutableListOf<Projectile>()
    var isFacingRight=true
    var health=100;
    var score=0
    var context=context
    var shootcooldown=0
    var shootreloadtime=40

    fun update(isMoving: Boolean, isJumping: Boolean, isAttacking: Boolean) {
        projectiles.removeAll { it.update() }
        if (shootcooldown > 0) {
            shootcooldown-- // Уменьшаем таймер перезарядки
        }
        currentAnimation = when {
            isAttacking -> attackAnimation
            isJumping -> jumpAnimation
            isMoving -> runAnimation
            else -> idleAnimation
        }
        currentAnimation.update()
    }

    fun draw(canvas: Canvas) {
        for (projectile in projectiles) {
            projectile.draw(canvas)
        }
        val frame = currentAnimation.getCurrentFrame()
        val flippedBitmap = if (!isFacingRight) {
            val matrix = Matrix()
            matrix.preScale(-1f, 1f)
            Bitmap.createBitmap(frame, 0, 0, frame.width, frame.height, matrix, true)
        } else {
            frame
        }

        canvas.drawBitmap(flippedBitmap, x, y, null)
    }

    fun shoot() {
        if(shootcooldown==0) {
            val direction = if (isFacingRight) 1 else -1
            projectiles.add(Projectile(x + 50 * direction, y+50f, direction, context))
            shootcooldown=shootreloadtime
        }
    }

    fun checkProjectileCollisions(enemies: MutableList<Enemy>) {
        for (i in projectiles.indices.reversed()) {
            for (j in enemies.indices.reversed()) {
                if (projectiles[i].checkCollision(enemies[j])) {
                    projectiles.removeAt(i)
                    score+=20
                    enemies.removeAt(j) // Удаляем врага, если попали в него
                    break
                }
            }
        }
    }

    fun checkfalling(){
        if (!isOnGround) {  //падение персонажа
            velocityY += gravity
            move(0f, velocityY)
        } else {
            velocityY = 0f
        }
    }
    fun move(dx: Float, dy: Float) {//Движение персонажа
        if(x>0f && x<2200f) {
            x += dx
            y += dy
            if(dx>0){
                isFacingRight=true;
            }
            else if(dx<0){
                isFacingRight=false;
            }
        }
        else if(x<=0){
            x=1f
        }
        else if(x>=2200){
            x=2199f
        }
    }
    fun takeDamage() {
        health-10
    }
}