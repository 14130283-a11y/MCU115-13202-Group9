package com.example.telephonebook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: MyAdapter
    private val contacts = ArrayList<Contact>()

    // 處理 SecActivity 回傳的新聯絡人
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val name = intent?.getStringExtra("name") ?: ""
            val phone = intent?.getStringExtra("phone") ?: ""
            val email = intent?.getStringExtra("email") ?: ""
            contacts.add(Contact(name, phone, email))
            myAdapter.refresh()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 測試資料：共 30 筆聯絡人（作業要求用 30 筆以上測試）
        contacts.add(Contact("Johnny", "0988312345", "johnnyhong@gmail.com"))
        contacts.add(Contact("Jessica", "0981911934", "jessicahuang@gmail.com"))
        for (i in 3..30) {
            contacts.add(Contact("聯絡人$i", "09${10000000 + i}", "user$i@example.com"))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val edSearch = findViewById<EditText>(R.id.edSearch)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        myAdapter = MyAdapter(contacts)
        recyclerView.adapter = myAdapter

        // 搜尋框內容一改變，就呼叫 Adapter 的 filter
        edSearch.doOnTextChanged { text, _, _, _ ->
            myAdapter.filter(text?.toString() ?: "")
        }

        btnAdd.setOnClickListener {
            val i = Intent(this, SecActivity::class.java)
            startForResult.launch(i)
        }
    }
}