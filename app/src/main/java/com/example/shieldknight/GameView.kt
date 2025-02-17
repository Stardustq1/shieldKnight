package com.example.shieldknight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet

import android.view.View

class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val player = Player(context, 100f, 500f) // Начальная позиция игрока
    private var velocityY = 0f // Скорость для прыжка
    private val gravity = 2f // Гравитация
    private val enemies = mutableListOf<Enemy>()
    public var isAttacking = false
    public var isMoving=false
    private var isJumping=false
    private val background:Bitmap=BitmapFactory.decodeResource(resources,R.drawable.background)
    private var scaledBackground: Bitmap? = null
    val soundManager = SoundManager(context)
    var grass:Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.tile_grass)
    var rock:Bitmap =BitmapFactory.decodeResource(context.resources, R.drawable.tile_rock)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scaledBackground = Bitmap.createScaledBitmap(background, w, h, true)
    }
    
    init {
        enemies.add(Enemy(context, 500f, 500f)) // Добавляем врага
        enemies.add(Enemy(context, 400f, 500f))
        enemies.add(Enemy(context, 200f, 500f))
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        canvas.drawBitmap(grass,100f+16,400f+16,null)
        scaledBackground?.let {
            canvas.drawBitmap(it, 0f, 0f, null) // Рисуем фон, если он уже загружен
        }
        for (enemy in enemies) {
            enemy.update(player.x)
            enemy.draw(canvas)
        }

        if (player.y < 500f) {  //падение персонажа
            velocityY += gravity
            player.move(0f, velocityY)
        } else {
            velocityY = 0f
        }
        player.checkProjectileCollisions(enemies)
        player.update(isMoving,isJumping,isAttacking) // Обновляем кадры анимации
        player.draw(canvas) // Рисуем игрока
        invalidate() // Перерисовка
    }

    fun movePlayer(dx: Float, dy: Float) {
        player.move(dx, dy)
        invalidate()
    }

    fun jumpPlayer() {
        if (player.y >= 500f) {
            isJumping=true
            soundManager.playSound("jump")
            velocityY = -30f
            player.move(0f, velocityY)
        }
    }

    fun shootPlayer() {
        player.shoot()
        soundManager.playSound("attack")
        isAttacking=false
    }
}
