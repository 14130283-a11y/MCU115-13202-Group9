package com.example.pageswitching

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var btnSwitch: Button
    private lateinit var tvReceived: TextView

    // STEP 01：宣告 ActivityResultLauncher 作為 Activity 啟動器
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInput = findViewById(R.id.etInput)
        btnSwitch = findViewById(R.id.btnSwitch)
        tvReceived = findViewById(R.id.tvReceived)

        // STEP 05：第二頁回傳時，在這裡接收資料
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val reply = result.data?.getStringExtra("reply_text")
                tvReceived.text = reply
            }
        }

        // STEP 02：用 Intent + Bundle 發送資料，並前往 SecActivity
        btnSwitch.setOnClickListener {
            val intent = Intent(this, SecActivity::class.java)
            val bundle = Bundle()
            bundle.putString("page_one_text", etInput.text.toString())
            intent.putExtras(bundle)
            launcher.launch(intent)
        }
    }
}