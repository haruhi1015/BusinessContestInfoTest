package com.example.infotest


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    //private lateinit var adapter: SearchAdapter
    private val itemList = listOf("セキュリティ", "数学", "英語", "専門", "結果", "プロフィール", "TID")
    private var filteredList = itemList.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerView)

        // 初期データをセット
        filteredList.addAll(itemList)

        /*
        // RecyclerView 設定
        adapter = SearchAdapter(filteredList) { selectedItem ->
            navigateToDetail(selectedItem)  // 選択したアイテムで画面遷移
        }

         */

        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.adapter = adapter
        recyclerView.visibility = View.GONE

        //ユーザーアイコンの読み込み
        val usericon : ImageButton =findViewById(R.id.userIcon)
        //ユーザーアイコンを押したら、ProfileActivityに移る
        usericon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        //セキュリティのボタンを読み込み
        val security :Button =findViewById(R.id.security)
        //セキュリティのボタンを押したら、UnitActivityに移る
        security.setOnClickListener {
            val intent = Intent(this, UnitActivity::class.java)
            intent.putExtra("CATEGORY","SecurityQuiz")
            startActivity(intent)
        }
        //デジタルマナーのボタンを読み込み
        val digiman :Button =findViewById(R.id.digiman)
        //デジタルマナーのボタンを押したら、UnitActivityに移る
        digiman.setOnClickListener {
            val intent = Intent(this, UnitActivity::class.java)
            intent.putExtra("CATEGORY","DigiManQuiz")
            startActivity(intent)
        }
        //電子機器の使い方のボタンを読み込み
        val howquiz :Button =findViewById(R.id.howquiz)
        //電子機器の使い方のボタンを押したら、UnitActivityに移る
        howquiz.setOnClickListener {
            val intent = Intent(this, UnitActivity::class.java)
            intent.putExtra("CATEGORY","HowQuiz")
            startActivity(intent)
        }

    }

    //リストをフィルタリングする関数
    private fun filterList(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(itemList)
            recyclerView.visibility = View.GONE
        } else {
            filteredList.addAll(itemList.filter { it.contains(query, ignoreCase = true) })
            //検索結果がない場合、RecyclerView を非表示にする
            if (filteredList.isEmpty()) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
            }
        }

        //adapter.notifyDataSetChanged()
    }

    //検索をクリアして RecyclerView を隠す
    private fun clearSearch() {
        searchView.setQuery("", false)
        searchView.clearFocus()
        recyclerView.visibility = View.GONE
    }

    private fun navigateToDetail(item: String) {
        when (item) {
            "SecurityQuiz" -> startActivity(Intent(this, UnitActivity::class.java))
            "DigiManQuiz" -> startActivity(Intent(this, UnitActivity::class.java))
            "HowQuiz" -> startActivity(Intent(this, UnitActivity::class.java))
        }
    }

    private fun showKeyboard(view: View) {
        view.postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }, 100) // 少し遅延させる
    }


    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

