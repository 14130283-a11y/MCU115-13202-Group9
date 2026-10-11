package com.example.pageswitching

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)

        val tvFromFirst = findViewById<TextView>(R.id.tvFromFirst)
        val etReply = findViewById<EditText>(R.id.etReply)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // 取出第一頁用 Bundle 送來的文字
        val bundle = intent.extras
        tvFromFirst.text = bundle?.getString("page_one_text")

        btnBack.setOnClickListener {
            // STEP 03：用 setResult() 儲存要回傳的資料
            val resultIntent = Intent()
            resultIntent.putExtra("reply_text", etReply.text.toString())
            setResult(RESULT_OK, resultIntent)

            // STEP 04：用 finish() 結束第二頁，回到第一頁
            finish()
        }
    }
}