package com.example.shieldknight

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        var btnplay=findViewById<Button>(R.id.btnplay)
        var btnsettings=findViewById<Button>(R.id.settings)
        var btnexit=findViewById<Button>(R.id.exit)

        var menu_sound = MediaPlayer.create(this, R.raw.menu_music)
        menu_sound.start()


        btnplay.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
            menu_sound.stop()
            finish() // Закрываем SplashActivity
        })
        btnsettings.setOnClickListener({
            startActivity(Intent(this, SettingsActivity::class.java))
            menu_sound.stop()
            finish() // Закрываем SplashActivity
        })
        btnexit.setOnClickListener({
            finish() // Закрываем SplashActivity
        })
    }
}