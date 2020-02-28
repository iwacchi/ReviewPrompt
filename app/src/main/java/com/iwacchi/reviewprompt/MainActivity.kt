package com.iwacchi.reviewprompt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.iwacchi.reviewprompt.preferenceKey.PreferenceUtility
import com.iwacchi.reviewprompt.reviewPrompt.ReviewPromptViewPager

class MainActivity : AppCompatActivity() {

    private lateinit var reviewPromptViewPager: ReviewPromptViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reviewPromptViewPager = findViewById(R.id.review_prompt_view_pager)
        PreferenceUtility().setSharedPreferences(this)

        // レビュー誘導を表示させるかどうかの判定
        val check = true
        if(check){
            // レビュー誘導をセットする
            reviewPromptViewPager.setAdapter(supportFragmentManager)
        } else {
            // レビュー誘導のViewを削除しちゃう(View.GONEとかではなく完全削除)
            (reviewPromptViewPager.parent as ViewGroup).removeView(reviewPromptViewPager)
        }
    }
}
