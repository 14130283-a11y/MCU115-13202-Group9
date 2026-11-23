package com.example.smartpomodorotimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var timerReceiver: BroadcastReceiver
    private var isReceiverRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer = findViewById(R.id.tvTimer)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)

        // 建立動態廣播接收器以接收每秒倒數更新[cite: 1, 2]
        timerReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val seconds = intent?.getIntExtra("REMAINING_SECONDS", 0) ?: 0
                val mins = seconds / 60
                val secs = seconds % 60
                tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs)

                if (seconds == 0) {
                    tvStatus.text = "Ready to Focus"
                }
            }
        }

        // 點擊 Start Focus：動態註冊接收器並啟動 TimerService[cite: 2]
        btnStart.setOnClickListener {
            if (!isReceiverRegistered) {
                val filter = IntentFilter(TimerService.ACTION_TIMER_UPDATE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 將原本的 RECEIVER_NOT_EXPORTED 改為 RECEIVER_EXPORTED
                    registerReceiver(timerReceiver, filter, Context.RECEIVER_EXPORTED)
                } else {
                    registerReceiver(timerReceiver, filter)
                }
                isReceiverRegistered = true
            }
            tvStatus.text = "Focusing..."
            val serviceIntent = Intent(this, TimerService::class.java).apply {
                putExtra("DURATION_SECONDS", 25 * 60) // 設定 25 分鐘[cite: 1, 2]
            }
            startService(serviceIntent)
        }

        // 點擊 STOP：停止背景計時服務[cite: 1, 2]
        btnStop.setOnClickListener {
            stopService(Intent(this, TimerService::class.java))
            tvStatus.text = "Ready to Focus"
            tvTimer.text = "25:00"
        }
    }

    override fun onDestroy() {
        // 結束時解除動態廣播註冊[cite: 1]
        if (isReceiverRegistered) {
            unregisterReceiver(timerReceiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }
}