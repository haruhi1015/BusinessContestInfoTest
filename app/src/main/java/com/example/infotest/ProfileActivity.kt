package com.example.infotest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile) // ← 必須！

        // SharedPreferences は onCreate の中で呼ぶ
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = prefs.getString("USERNAME", "未ログイン")

        // TextView に反映
        val usernameText: TextView = findViewById(R.id.userName)
        usernameText.text = "ユーザー名: $username"


        //戻るボタンの読み込み
        val returnBtn : Button =findViewById(R.id.returnBtn)
        //ユーザーアイコンを押したら、ProfileActivityに移る
        returnBtn.setOnClickListener {
            finish()
        }

        //リセットボタンの読み込み
        val resetBtn : Button =findViewById(R.id.resetBtn)
        //リセットボタンを押したら、すべての回答状況をリセットする
        resetBtn.setOnClickListener {
            QuizData.DigiManQuiz.forEach { it.state = 0 }
            QuizData.SecurityQuiz.forEach { it.state = 0 }
            QuizData.HowQuiz.forEach { it.state = 0 }
            updateChart()
        }

        //プロフィール編集ボタンの読み込み
        val editProfileBtn : Button=findViewById(R.id.editProfileBtn)
        //ボタンを押したら、ProfileActivity2に移る
        editProfileBtn.setOnClickListener {
            val intent = Intent(this, ExchangeActivity::class.java)
            //val intent = Intent(this, ProfileActivity2::class.java)
            startActivity(intent)
        }
        updateChart()
    }

    private fun countStates(list: List<Mondai>): Triple<Int, Int, Int> {
        val correct = list.count { it.state == 1 }
        val wrong = list.count { it.state == 2 }
        val unanswered = list.count { it.state == 0 }
        return Triple(correct, wrong, unanswered)
    }

    private fun updateChart() {
        //すべての教科の正解不正解未回答を取得
        val digiStats = countStates(QuizData.DigiManQuiz)
        val securityStats = countStates(QuizData.SecurityQuiz)
        val howStats = countStates(QuizData.HowQuiz)

        //すべての教科の正解数の合計を計算
        val scoreTotal = digiStats.first + securityStats.first + howStats.first

        val barChart: BarChart = findViewById(R.id.barChart)

        val entries = listOf(
            BarEntry(0f, floatArrayOf(securityStats.first.toFloat(), securityStats.second.toFloat(), securityStats.third.toFloat())),
            BarEntry(1f, floatArrayOf(digiStats.first.toFloat(), digiStats.second.toFloat(), digiStats.third.toFloat())),
            BarEntry(2f, floatArrayOf(howStats.first.toFloat(), howStats.second.toFloat(), howStats.third.toFloat()))
        )

        val dataSet = BarDataSet(entries, "単元ごとの結果")
        dataSet.stackLabels = arrayOf("正解", "不正解", "未回答")
        //正解不正解未回答の色分け
        dataSet.setColors(intArrayOf(
            android.R.color.holo_green_light, // 正解
            android.R.color.holo_red_light,   // 不正解
            android.R.color.darker_gray       // 未回答
        ).map { ContextCompat.getColor(this, it) })

        val data = BarData(dataSet)
        barChart.data = data
        barChart.invalidate()


        // X軸のラベル非表示
        barChart.xAxis.setDrawLabels(false)
        // Y軸のラベル非表示（左右両方）
        barChart.axisLeft.setDrawLabels(false)
        barChart.axisRight.setDrawLabels(false)


        //得点表記の書き換え
        val totalText: TextView = findViewById(R.id.totalScoreLabel)
        totalText.text = "合計点数: $scoreTotal"
        // セキュリティの点数に反映
        val securityText: TextView = findViewById(R.id.totalScoreSecurityLabel)
        securityText.text = "セキュリティの点数: ${securityStats.first}"
        // デジタルマナーの点数に反映
        val digimanText: TextView = findViewById(R.id.totalScoreDigimanLabel)
        digimanText.text = "デジタルマナーの点数: ${digiStats.first}"
        // 電子機器の使い方の点数に反映
        val howquizText: TextView = findViewById(R.id.totalScoreHowquizLabel)
        howquizText.text = "電子機器の使い方の点数: ${howStats.first}"
    }


}
