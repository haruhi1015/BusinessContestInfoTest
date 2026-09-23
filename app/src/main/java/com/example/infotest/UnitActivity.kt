package com.example.infotest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class UnitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit)

        val category = intent.getStringExtra("CATEGORY")

        //単元名をcategoryをもとにstringsから取得
        val categoryName = when(category){
            "SecurityQuiz" -> getString(R.string.category_security)
            "DigiManQuiz" -> getString(R.string.category_digiman)
            "HowQuiz" -> getString(R.string.category_howquiz)
            else -> "未定義の単元"
        }

        //単元名を画面に反映
        val unitTitle: TextView = findViewById(R.id.unitTitle)
        unitTitle.text = categoryName

        val returnBtn: Button = findViewById(R.id.returnBtn)
        val quizBtn: Button = findViewById(R.id.quizBtn)
        val videoBtn: Button = findViewById(R.id.videoBtn)

        //戻るボタンの設定
        returnBtn.setOnClickListener {
            finish()
        }

        //問題ボタンが押された場合、QuizActivityに遷移
        quizBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("CATEGORY", category)
            startActivity(intent)
        }

        //授業動画を選択された場合、VideoMenuに遷移
        videoBtn.setOnClickListener {
            val intent = Intent(this, VideoMenuActivity::class.java)
            intent.putExtra("CATEGORY", category)
            startActivity(intent)
        }
    }
}
