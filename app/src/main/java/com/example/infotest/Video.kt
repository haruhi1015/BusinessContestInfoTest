package com.example.infotest

data class Video(
    val title: String,  //動画のタイトル
    val movieResId: Int      //res/rawにおいてある動画のID
)

object VideoData{
    val DigiManQuiz = listOf(
        Video("デジタルマナー",R.raw.degiman)
    )

    val SecurityQuiz = listOf(
        Video("セキュリティ",R.raw.security)
    )

    val HowQuiz = listOf(
        Video("電子機器の使い方",R.raw.howquiz),//送られてきたらresIdだけ変え

    )
}