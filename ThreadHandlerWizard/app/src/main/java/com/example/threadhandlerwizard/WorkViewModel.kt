package com.example.threadhandlerwizard

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WorkViewModel(application: Application) : AndroidViewModel(application) {
    private val handlerThread = HandlerThread("VM-Work").apply { start() }
    private val worker = Handler(handlerThread.looper)

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> = _status

    @Volatile private var running = false
    private var mediaPlayer: MediaPlayer? = null

    fun start() {
        if (running) return
        running = true
        _status.postValue("準備中...")
        _progress.postValue(0)
        worker.post {
            try {
                Thread.sleep(10000) // 準備工作 10 秒
                if (!running) {
                    _status.postValue("Canceled")
                    return@post
                }

                _status.postValue("工作中")
                startMusic()

                for (i in 1..100) {
                    if (!running) break
                    Thread.sleep(3500) // 背景工作進度
                    _progress.postValue(i)
                }

                stopMusic()
                _status.postValue(if (running) "背景工作結束!" else "Canceled")
                running = false
            } catch (_: InterruptedException) {
                stopMusic()
                _status.postValue("Canceled")
                running = false
            }
        }
    }

    private fun startMusic() {
        stopMusic()
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.bgm)?.apply {
            isLooping = true
            start()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    fun cancel() {
        running = false
        stopMusic()
    }

    override fun onCleared() {
        running = false
        stopMusic()
        handlerThread.quitSafely()
        super.onCleared()
    }
}