package com.example.infotest

/*
・問題数
・画面の表示設定、xmlの読み込み、余白調整
・画面のボタンの判定(クリックイベント)
・クイズデータのシャッフル、出題
・制誤判定、ダイアログ表示
・５問終わったら合計スコアを端末の保存して結果画面に遷移
・最初の問題の画面表示
*/

import android.view.View
import android.widget.Button
import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import android.content.Context
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.example.infotest.QuizData
import com.example.infotest.databinding.ActivityQuizBinding
import com.example.infotest.databinding.ActivityResultBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.collections.get
import kotlin.text.get
// AppCompatActivityじゃなくてComponentActivityにしてた
class QuizActivity : AppCompatActivity() {

    companion object {
        const val QUIZ_COUNT = 3 //今２問にしてるだけ
    }
    // 現在の問題を保持する変数
    private lateinit var quiz: Mondai

    //今のカテゴリを複数クラスで使いたいため初期化
    private var category: String? = null

    // 今の分野のクイズリスト
    private lateinit var quizList: MutableList<Mondai>

    private lateinit var binding: ActivityQuizBinding
    // 今の問題の正解文字列
    private var rightAnswer: String? = null
    // 正解数
    private var rightAnswerCount = 0
    // 今回のトータル獲得ポイント
    private var currentTotalPoint = 0
    // 今何問目か
    private var quizCount = 1


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root) // xmlを読み込んで画面にセット

        //カテゴリに現在の単元を代入
        category = intent.getStringExtra("CATEGORY")

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //カテゴリーによって問題リストを切り替え
        when (category) {
            "SecurityQuiz" -> quizList = QuizData.SecurityQuiz.toMutableList()
            "DigiManQuiz" -> quizList = QuizData.DigiManQuiz.toMutableList()
            "HowQuiz" -> quizList = QuizData.HowQuiz.toMutableList()
            else -> quizList = mutableListOf()
        }

        // 選択肢にクイックリスナーを設定押されたらcheckAnswerが呼ばれる
        binding.answerBtn1.setOnClickListener{ checkAnswer(it) }
        binding.answerBtn2.setOnClickListener{ checkAnswer(it) }
        binding.answerBtn3.setOnClickListener{ checkAnswer(it) }
        //binding.answerBtn4.setOnClickListener{ checkAnswer(it) }

        // 問題をシャッフル、ランダムに出るように
        quizList.shuffle()

        showNextQuiz()
    }


    private fun showNextQuiz(){
        // 今何問目？
        binding.countLabel.text = getString(R.string.count_label,quizCount)
        // 一問分丸ごと取り出す
        quiz = quizList[0]
        // 問題文だけをセット
        binding.questionLabel.text = quiz.question
        // 正解をセット
        rightAnswer = quiz.correctAnswer

        /*問題文を削除(いるかわからん)
        quiz.removeAt(0)*/
        // 選択肢をシャフル
        val options = quiz.options.shuffled()

        // 正解と選択肢をセット、割り当て
        binding.answerBtn1.text = options[0]
        binding.answerBtn2.text = options[1]
        binding.answerBtn3.text = options[2]
        //binding.answerBtn4.text = options[3]

        // 出題済みを削除
        quizList.removeAt(0)
    }

    // 回答ボタンが押されたらの処理
    private fun checkAnswer(view:View){
        // どのボタンが押された？
        val answerBtn: Button = findViewById(view.id)
        // 押されたボタンの文字列
        val btnText = answerBtn.text.toString()

        // ダイアログタイトル生成
        val alertTitle: String
        if(btnText == rightAnswer){
            alertTitle = "正解！"
            rightAnswerCount++
            quiz.state=1 //問題のステータスを正解に変更
            currentTotalPoint += quiz.point
        }else{
            alertTitle = "不正解..."
            quiz.state=2
        }

        // ダイアログを作成し、OKをおすとcheckQuizCount()を呼び出す
        MaterialAlertDialogBuilder(this)
            .setTitle(alertTitle)
            .setMessage("答え：${quiz.correctAnswer}\n\n解説：${quiz.explanation}")
            .setPositiveButton("OK"){dialogInterface,i ->
                checkQuizCount()
            }
            .setCancelable(false)
            .show()
    }


    // 出題数のチェック、終わりかそうでないか
    private fun checkQuizCount(){
        if(quizCount == QUIZ_COUNT){
            //ログイン時保存したuser_prefsを参照
            val userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            //ユーザーの名前を取得
            val currentUser = userPrefs.getString("USERNAME", "unknown")
            //ユーザーのkeyを作成
            val userPointKey = "total_point_${currentUser}"

            //トータルポイントを保存する場所(point_prefsを開く)
            val prefs = getSharedPreferences("point_prefs",Context.MODE_PRIVATE)
            val editor = prefs.edit()

            //トータルポイントを変更
            var TotalPoint = prefs.getInt(userPointKey, 0)
            TotalPoint += currentTotalPoint
            editor.putInt(userPointKey,TotalPoint)

            editor.apply()

            // 結果画面への遷移(ResultActivity)
            val intent = Intent(this,ResultActivity::class.java)
            intent.putExtra("RIGHT_ANSWER_COUNT",rightAnswerCount)
            intent.putExtra("CATEGORY",category)
            startActivity(intent)
            finish() //追加
        }else{
            quizCount++
            showNextQuiz()
        }
    }
}