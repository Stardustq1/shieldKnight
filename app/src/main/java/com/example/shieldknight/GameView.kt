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
import android.widget.TextView

class Platform(val x: Float, val y: Float, val width: Float, val height: Float)
class GameView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val player = Player(context, 100f, 500f) // Начальная позиция игрока
    private var spawntimer=0
    private var spawninterval=200
    private val enemies = mutableListOf<Enemy>()
    public var texthealth=findViewById<TextView>(R.id.textHealth)
    var db=GameDatabase(context)
    private var screenHeight=resources.displayMetrics.heightPixels
    public var isAttacking = false
    public var isMoving=false
    public var isPause=false
    private var isJumping=false
    private val background:Bitmap=BitmapFactory.decodeResource(resources,R.drawable.background)
    private val tileSet = Tile(context)

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
            if(player.y==500f){
                player.velocityY=0f
                player.isOnGround=true
            }
            spawnenemies()
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

    fun getplayerscore():Int{
        return player.score
    }

    fun movePlayer(dx: Float, dy: Float) {
        player.move(dx, dy)
        invalidate()
    }

    fun jumpPlayer() {
        if (player.y >= 500f) {
            player.isOnGround=false
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
