package com.example.infotest

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import android.content.Intent
import android.widget.LinearLayout

class VideoMenuActivity : AppCompatActivity() {

    // 再生中かどうかの判定
    private var isPlaying = false

    //選択中の動画
    private var selectVideo: Video? = null

    //今の単元の動画リスト
    private var videos :List<Video> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video) // xmlの読み込み

        //IntentでUnitActivityからカテゴリを受け取り
        val category = intent.getStringExtra("CATEGORY")

        //viewを取得
        val videoView = findViewById<VideoView>(R.id.videoView)
        val listView = findViewById<ListView>(R.id.videoListView)
        val quizButton = findViewById<Button>(R.id.quizButton)

        //再生と一時停止ボタンを追加
        val playButton = Button(this).apply{
            text = "再生 / 一時停止"
        }
        val layout = findViewById<LinearLayout>(R.id.rootLayout)
        layout.addView(playButton,1)

        //カテゴリをもとにVideoDataから動画リストを取得
        videos = when(category){
            "SecurityQuiz" -> VideoData.SecurityQuiz
            "DigiManQuiz" -> VideoData.DigiManQuiz
            "HowQuiz" -> VideoData.HowQuiz
            else -> emptyList()
        }

        //elseの時の処理を一応書いておくべきだとは思うけどいったん無視

        //タイトルをListViewに表示
        val titles = videos.map {it.title}
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, titles)
        listView.adapter = adapter

        // 動画リストをクリックしたら再生
        listView.setOnItemClickListener { _, _, position, _ ->
            val v = videos[position]
            selectVideo = v

            // uriに動画の場所をわたす
            val uri = "android.resource://${packageName}/${v.movieResId}".toUri()
            videoView.setVideoURI(uri)
            videoView.start()
            isPlaying = true
            playButton.text = "一時停止"

        }

        playButton.setOnClickListener {
            if (isPlaying) {
                videoView.pause()
                isPlaying = false
                playButton.text = "再生"
            } else {
                videoView.start()
                isPlaying = true
                playButton.text = "一時停止"
            }
        }

        // 演習問題へ遷移？
        quizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("CATEGORY", category)
            startActivity(intent)
        }
    }
}
