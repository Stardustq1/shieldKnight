package com.example.shieldknight
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var battle_theme = MediaPlayer.create(this, R.raw.battle_theme)
        battle_theme.start()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)
        var moveRight = false
        var moveLeft=false
        val db=GameDatabase(this)
        //Кнопки управления
        val gameView = findViewById<GameView>(R.id.gameView)
        val btnLeft = findViewById<Button>(R.id.btnleft)
        val btnRight = findViewById<Button>(R.id.btnright)
        val btnJump = findViewById<Button>(R.id.btnjump)
        val btnshoot = findViewById<Button>(R.id.btnshoot)

        //Кнопки меню паузы
        var pauseMenu: FrameLayout =findViewById(R.id.pausemenu)
        var pauseButton:Button=findViewById(R.id.btnpause)
        var resumeButton:Button=findViewById(R.id.btnresume)
        var restartButton:Button=findViewById(R.id.btnrestart)
        var exitButton:Button=findViewById(R.id.btnexit)

        val movementRunnable = object : Runnable {
            override fun run() {
                if (moveLeft) gameView.movePlayer(-20f, 0f) // Двигаем влево
                if (moveRight) gameView.movePlayer(20f, 0f)  // Двигаем вправо
                if (moveLeft || moveRight) handler.postDelayed(this, 50) // Повторяем только если кнопка зажата
            }
        }

        pauseButton.setOnClickListener({
            gameView.isPause=true
            pauseMenu.visibility=View.VISIBLE
        })
        resumeButton.setOnClickListener({
            gameView.isPause=false
            pauseMenu.visibility=View.GONE
        })
        restartButton.setOnClickListener({
            finish()
            startActivity(intent)
        })
        exitButton.setOnClickListener({
            db.addScore("player",gameView.getplayerscore())
            finish()
            startActivity(Intent(this, MenuActivity::class.java))
        })


        btnshoot.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                {
                    gameView.shootPlayer()
                    gameView.isAttacking=true
                }
                MotionEvent.ACTION_UP -> {
                    gameView.isAttacking=false
                }
            }
            true
        }

        btnLeft.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveLeft = true
                    handler.post(movementRunnable)
                    gameView.isMoving=true
                }
                MotionEvent.ACTION_UP -> {
                    moveLeft = false
                    gameView.isMoving=false
                }
            }
            true
        }

        btnRight.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    moveRight = true
                    handler.post(movementRunnable)
                    gameView.isMoving=true
                }
                MotionEvent.ACTION_UP -> {
                    moveRight = false
                    gameView.isMoving=false
                }
            }
            true
        }

        btnJump.setOnClickListener {
            gameView.jumpPlayer() // Прыжок
        }
    }
}