package com.example.smartpomodorotimer

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        // 點擊返回主頁按鈕關閉此畫面[cite: 2, 3]
        findViewById<Button>(R.id.btnBackHome).setOnClickListener {
            finish()
        }
    }
}