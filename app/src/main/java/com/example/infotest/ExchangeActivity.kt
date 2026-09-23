package com.example.infotest

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.infotest.databinding.ActivityExchangeBinding

class ExchangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExchangeBinding

    //
    private val PREF_NAME = "point_prefs"

    //現在の所持ポイント
    private var currentUserPoints = 0

    //各アイテムのコスト
    private val item_cost_1 = 500
    private val item_cost_2 = 800
    private val item_cost_3 = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- クリックリスナーの設定 ---
        binding.exchangeBtn1.setOnClickListener {
            onExchangeAttempt(item_cost_1)
        }
        binding.exchangeBtn2.setOnClickListener {
            onExchangeAttempt(item_cost_2)
        }
        binding.exchangeBtn3.setOnClickListener {
            onExchangeAttempt(item_cost_3)
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCurrentPoints()
    }

    //ユーザー別のポイントのキーを取得するヘルパー関数
    private fun getUserPointKey(): String {
        //MainActivityで保存した "user_prefs" を読みに行く
        val userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        //"USERNAME"キーで、ユーザー名を取得
        val currentUser = userPrefs.getString("USERNAME", "unknown")

        //"total_point_..."というキーを返す
        return "total_point_${currentUser}"
    }

    //SharedPreferencesから現在の総ポイントを読み込む
    private fun loadCurrentPoints() {
        // "point_prefs" を開く
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // ★ 3. ユーザー専用のキーを取得
        val userKey = getUserPointKey()

        // ★ 4. そのキーでポイントを読み込む
        currentUserPoints = prefs.getInt(userKey, 0)

        updatePointDisplay(currentUserPoints)
    }

    /**
     * 新しいポイント数を SharedPreferences に保存する
     */
    private fun savePoints(newPoints: Int) {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // ★ 5. ユーザー専用のキーを取得
        val userKey = getUserPointKey()

        // ★ 6. そのキーにポイントを保存
        editor.putInt(userKey, newPoints)

        editor.apply()
    }

    //画面のTextView表示を更新し、クラス内の変数も更新する
    private fun updatePointDisplay(points: Int) {
        currentUserPoints = points
        binding.currentPointsValue.text = "$points pt"
    }

    //アイテム交換処理
    private fun onExchangeAttempt(itemCost: Int) {
        if (currentUserPoints >= itemCost) {
            val newTotalPoints = currentUserPoints - itemCost
            savePoints(newTotalPoints)
            updatePointDisplay(newTotalPoints)
            Toast.makeText(this, "アイテムと交換しました！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "ポイントが足りません...", Toast.LENGTH_SHORT).show()
        }
    }
}