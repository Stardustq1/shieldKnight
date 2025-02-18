package com.example.shieldknight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log

import android.view.View
class Platform(val x: Float, val y: Float, val width: Float, val height: Float)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val player = Player(context, 100f, 500f) // Начальная позиция игрока
    private val paint = Paint()

    private var spawntimer=0
    private var spawninterval=100
    private val enemies = mutableListOf<Enemy>()
    private var screenHeight=resources.displayMetrics.heightPixels
    public var isAttacking = false
    public var isMoving=false
    public var isPause=false
    private var isJumping=false
    private val background:Bitmap=BitmapFactory.decodeResource(resources,R.drawable.background)
    private val tileSet = Tile(context)
    private val platforms = listOf(
        Platform(100f, 500f, 100f, 20f),
        Platform(250f, 400f, 100f, 20f)
    )
    private val tileMap = TileMap(tileSet, listOf(
        listOf( 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0, 0,0,0,0,0,0,0 ,0,0,0,0,0,0,0),
        listOf( 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
        listOf( 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
        listOf( 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
        listOf( 1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
    ), 16,50, screenHeight )
    private var scaledBackground: Bitmap? = null
    val soundManager = SoundManager(context)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scaledBackground = Bitmap.createScaledBitmap(background, w, h, true)
    }
    
    init {
        paint.color = Color.RED  // Устанавливаем цвет
        paint.style = Paint.Style.STROKE  // Рисуем только контур
        paint.strokeWidth = 5f  // Толщина линии
        enemies.add(Enemy(context, 500f, 500f)) // Добавляем врага
        enemies.add(Enemy(context, 400f, 500f))
        enemies.add(Enemy(context, 200f, 500f))
    }

    override fun onDraw(canvas: Canvas) {
        if (!isPause){
            super.onDraw(canvas)
            scaledBackground?.let {
                canvas.drawBitmap(it, 0f, 0f, null) // Рисуем фон, если он уже загружен
            }
            tileMap.draw(canvas)
            for (enemy in enemies) {
                enemy.update(player.x)
                enemy.draw(canvas)
            }
            spawnenemies()
            player.checkPlatformCollision(platforms)
            player.checkfalling()
            player.checkProjectileCollisions(enemies)
            player.update(isMoving,isJumping,isAttacking) // Обновляем кадры анимации
            player.draw(canvas) // Рисуем игрока
            invalidate() // Перерисовка
        }
    }
    fun spawnenemies(){
        spawntimer++
        if(spawntimer==spawninterval){
            enemies.add(Enemy(context, 100f, 500f))
            enemies.add(Enemy(context, 2000f, 500f))
            spawntimer=0;
        }
    }

    fun movePlayer(dx: Float, dy: Float) {
        player.move(dx, dy)
        invalidate()
    }

    fun jumpPlayer() {
        if (player.y >= 500f) {
            player.isOnGround=false
            isJumping=true
            soundManager.playSound("jump")
            player.velocityY = -30f
            player.move(0f, player.velocityY)
        }
    }

    fun shootPlayer() {
        player.shoot()
        soundManager.playSound("attack")
        isAttacking=false
    }
}
