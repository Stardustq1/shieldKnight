package com.example.shieldknight

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text

class RecordsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_records)
        var textview=findViewById<TextView>(R.id.textViewScore)
        val db = GameDatabase(this)
        val topScores = db.getTopScores()

        for ((player, score) in topScores) {
            textview.text="${player}:${score}"
        }
    }
}