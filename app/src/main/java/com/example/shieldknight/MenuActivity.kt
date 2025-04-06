package com.example.shieldknight

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat



class MenuActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 123
    private val CHANNEL_ID = "my_channel_id"
    private val NOTIFICATION_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        fun sendNotification() {
            Toast.makeText(this, "Уведомление может быть отправлено", Toast.LENGTH_SHORT).show()
            // Здесь код для отправки уведомления
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Проверяем, есть ли разрешение на уведомления
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {

                // Запрашиваем разрешение, если оно не предоставлено
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                // Разрешение уже есть
                sendNotification()
            }
        } else {
            // На старых версиях Android не нужно запрашивать разрешение
            sendNotification()
        }

    // Метод для отправки уведомлений


    // Обработка результата запроса разрешения
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено
                sendNotification()
            } else {
                // Разрешение отклонено
                Toast.makeText(this, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show()
            }
        }
    }

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "Channel for heads-up notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Регистрируем канал уведомлений
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        fun sendHeadsUpNotification() {
            val context:Context=this
            // Создаем уведомление с приоритетом HIGH (для всплывающего уведомления)
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Иконка уведомления
                .setContentTitle("Всплывающее уведомление")
                .setContentText("Это уведомление появится сверху экрана.")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Важно для всплывающих уведомлений
                .setDefaults(Notification.DEFAULT_ALL) // Звуки, вибрация и световой индикатор
                .setAutoCancel(true) // Уведомление исчезает после нажатия
                .build()

            // Показываем уведомление
            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(NOTIFICATION_ID, notification)
            }
        }
        // Настроим кнопку для отправки уведомления
        val notifyButton: Button = findViewById(R.id.btnplay)
        notifyButton.setOnClickListener {
            sendHeadsUpNotification()
        }


        val videoView = findViewById<VideoView>(R.id.videoBackground)
        val uri = Uri.parse("android.resource://$packageName/${R.raw.background_menu}")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { it.isLooping = true } // Зацикливаем видео
        videoView.start()
        var menu_sound = MediaPlayer.create(this, R.raw.menu_music)
        menu_sound.start()
        var btnplay=findViewById<Button>(R.id.btnplay)
        var btnsettings=findViewById<Button>(R.id.settings)
        var btnexit=findViewById<Button>(R.id.exit)
        var btnrecords=findViewById<Button>(R.id.btnrecords)
        btnrecords.setOnClickListener({
            startActivity(Intent(this, RecordsActivity::class.java))
            menu_sound.stop()
            finish() // Закрываем SplashActivity
        })
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
            menu_sound.stop()
            finish() // Закрываем SplashActivity
        })
    }
}