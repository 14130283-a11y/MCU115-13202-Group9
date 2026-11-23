package com.example.smartpomodorotimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // 裝置重啟後記錄或提醒使用者開始每日學習課程[cite: 2]
            Log.d("BootReceiver", "裝置已重新開機，準備開始每日學習課程！")
        }
    }
}