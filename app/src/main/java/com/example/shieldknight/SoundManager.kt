package com.example.shieldknight

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundManager(context: Context) {
    private val soundPool: SoundPool
    private val soundMap = HashMap<String, Int>()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)  // Максимум 5 звуков одновременно
            .setAudioAttributes(audioAttributes)
            .build()

        // Загружаем звуки
        soundMap["jump"] = soundPool.load(context, R.raw.jump_sound, 1)
        soundMap["attack"] = soundPool.load(context, R.raw.death_sound, 1)
    }

    fun playSound(name: String) {
        val soundId = soundMap[name] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)  // Громкость 1.0, без повтора, нормальная скорость
    }

    fun release() {
        soundPool.release()
    }
}