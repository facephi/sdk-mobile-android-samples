package com.facephi.demovoice.media

import android.media.MediaPlayer
import android.util.Log

object AppMediaPlayer {
    private var mediaPlayer = MediaPlayer()

    private var audios: List<String>? = null
    private var audioIndex = 0
    private lateinit var indexOutput: (Int) -> Unit
    private lateinit var onStop: () -> Unit

    fun init(audios: List<String>, indexOutput: (Int) -> Unit, onStop: () -> Unit) {
        AppMediaPlayer.audios = audios
        audioIndex = 0
        AppMediaPlayer.indexOutput = indexOutput
        AppMediaPlayer.onStop = onStop
    }

    private fun play(file: String, onFinish: () -> Unit) {
        Log.d("APP","MEDIA PLAY file:$file")
        // val mediaSource = AppMediaDataSource(byteArray)
        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(file)
            setOnCompletionListener {
                onFinish()
                release()
            }
            prepareAsync()
            setOnPreparedListener {
                indexOutput.invoke(audioIndex)
                start()
            }
        }
    }

    fun stop() {
        Log.d("APP","MEDIA STOP")

        audioIndex = 0
        if (mediaPlayer.isPlaying == true) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    fun playAudios() {
        if (audioIndex < audios!!.size) {
            play(audios!![audioIndex]) {
                audioIndex += 1
                playAudios()
            }
        } else {
            stop()
            onStop()
        }
    }
}
