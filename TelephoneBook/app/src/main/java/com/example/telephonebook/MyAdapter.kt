package com.example.telephonebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    // 完整的聯絡人資料
    private val allData: ArrayList<Contact>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // 目前畫面上顯示的資料（搜尋過濾後的結果）
    private val shownData = ArrayList<Contact>(allData)
    private var keyword = ""

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName: TextView = v.findViewById(R.id.tvName)
        private val tvPhone: TextView = v.findViewById(R.id.tvPhone)
        private val tvEmail: TextView = v.findViewById(R.id.tvEmail)
        private val imgDelete: ImageView = v.findViewById(R.id.imgDelete)

        fun bind(item: Contact, clickListener: (Contact) -> Unit) {
            tvName.text = item.name
            tvPhone.text = item.phone
            tvEmail.text = item.email
            imgDelete.setOnClickListener {
                clickListener.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_row, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = shownData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shownData[position]) { item ->
            // 從完整資料中刪除，再依目前的搜尋關鍵字重新整理畫面
            allData.remove(item)
            refresh()
        }
    }

    // 搜尋：姓名、電話、Email 任一包含關鍵字就顯示
    fun filter(text: String) {
        keyword = text.trim()
        refresh()
    }

    // 依目前的關鍵字重新產生顯示清單
    fun refresh() {
        shownData.clear()
        if (keyword.isEmpty()) {
            shownData.addAll(allData)
        } else {
            for (c in allData) {
                if (c.name.contains(keyword, ignoreCase = true) ||
                    c.phone.contains(keyword) ||
                    c.email.contains(keyword, ignoreCase = true)
                ) {
                    shownData.add(c)
                }
            }
        }
        notifyDataSetChanged()
    }
}