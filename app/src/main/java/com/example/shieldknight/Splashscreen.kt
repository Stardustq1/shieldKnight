package com.example.shieldknight

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splashscreen)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.intro
        val uri = Uri.parse(videoPath)
        videoView.setVideoURI(uri)
        var mediaPlayer = MediaPlayer.create(this, R.raw.intro_music)
        mediaPlayer?.start()
        videoView.setOnCompletionListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
        videoView.start() // Запуск видео
    }
}