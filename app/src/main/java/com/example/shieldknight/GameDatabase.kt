package com.example.shieldknight

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GameDatabase(context: Context) : SQLiteOpenHelper(context, "game.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                player TEXT UNIQUE, 
                score INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS scores")
        onCreate(db)
    }

    fun addScore(player: String, score: Int) {
        val db = writableDatabase
        val highscore = "SELECT score FROM scores WHERE player = ?"
        val cursor = db.rawQuery(highscore, arrayOf(player))

        var bestScore = 0
        if (cursor.moveToFirst()) {
            bestScore = cursor.getInt(0)
        }
        cursor.close()
        if(score>bestScore){
            val query = """
        REPLACE INTO scores (player, score) 
        VALUES (?, ?)
    """.trimIndent()

            val statement = db.compileStatement(query)
            statement.bindString(1, player)
            statement.bindLong(2, score.toLong())
            statement.execute()
            db.close()
        }
    }

    fun getTopScores(limit: Int = 10): List<Pair<String, Int>> {
        val db = readableDatabase
        val scoresList = mutableListOf<Pair<String, Int>>()

        val cursor = db.rawQuery("SELECT player, score FROM scores ORDER BY score DESC LIMIT ?", arrayOf(limit.toString()))

        while (cursor.moveToNext()) {
            val player = cursor.getString(0)
            val score = cursor.getInt(1)
            scoresList.add(Pair(player, score))
        }

        cursor.close()
        db.close()
        return scoresList
    }
}