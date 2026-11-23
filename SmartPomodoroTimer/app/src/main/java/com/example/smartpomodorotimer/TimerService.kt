package com.example.smartpomodorotimer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast


class TimerService : Service() {
    private var isRunning = false
    private var remainingSeconds = 25 * 60
    private var screenOnReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        // 動態註冊系統廣播：監聽螢幕開啟 (ACTION_SCREEN_ON)
        screenOnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (Intent.ACTION_SCREEN_ON == intent.getAction()) {
                    val mins = remainingSeconds / 60
                    Toast.makeText(context, "保持專注！還有 " + mins + " 分鐘", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        registerReceiver(screenOnReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("DURATION_SECONDS")) {
            remainingSeconds = intent.getIntExtra("DURATION_SECONDS", 25 * 60)
        }
        if (!isRunning) {
            isRunning = true
            Thread(Runnable {
                while (isRunning && remainingSeconds > 0) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        break
                    }
                    if (!isRunning) break
                    remainingSeconds--

                    // 每秒發送自訂廣播更新 UI
                    val updateIntent = Intent(ACTION_TIMER_UPDATE)
                    updateIntent.setPackage(getPackageName())
                    updateIntent.putExtra("REMAINING_SECONDS", remainingSeconds)
                    sendBroadcast(updateIntent)
                }
                // 倒數歸零：啟動 FinishActivity 並停止自己
                if (isRunning && remainingSeconds == 0) {
                    val finishIntent = Intent(this@TimerService, FinishActivity::class.java)
                    finishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(finishIntent)
                    stopSelf()
                }
            }).start()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        if (screenOnReceiver != null) {
            unregisterReceiver(screenOnReceiver)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_TIMER_UPDATE: String = "TIMER_UPDATE"
    }
}