package com.example.infotest

import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import com.example.infotest.databinding.ActivityResultBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java


/*
結果を表示する画面
 */
class ResultActivity : ComponentActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //システムバー分の余白を足すリスナーの登録
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //正解数の取得、もしなければ0
        val score = intent.getIntExtra("RIGHT_ANSWER_COUNT", 0)

        //カテゴリの取得
        val category = intent.getStringExtra("CATEGORY")
        //入ってきたcategoryをもとにトータルスコアの取得
        val totalScore = when (category) {
            "DigiManQuiz" -> QuizData.DigiManQuiz.count { it.state == 1 }
            "SecurityQuiz" -> QuizData.SecurityQuiz.count { it.state == 1 }
            "HowQuiz" -> QuizData.HowQuiz.count { it.state == 1 }
            else -> 0
        }
        //トータルスコアの読み出し、上書き
        //val prefs = getSharedPreferences("preference_file_key", MODE_PRIVATE)
        //var totalScore = prefs.getInt("TOTAL_SCORE", 0)
        //val editor = prefs.edit()


        //トータルスコアに加算
        //totalScore += score

        //TextViewに表示
        binding.resultLabel.text = getString(R.string.result_score,score)
        binding.totalScoreLabel.text = getString(R.string.result_total_score,totalScore)

        //トータルスコアを保存
        //editor.putInt("TOTAL_SCORE",totalScore)
        //editor.apply()

        //returnBtnにアクセス
        val returnBtn:Button = findViewById(R.id.returnBtn)
        //ボタンが押されたら...
       returnBtn.setOnClickListener {
            val intent = Intent(this,HomeActivity::class.java)//HomeActivityと連携
            startActivity(intent)
        }

    }
}
