package com.iwacchi.reviewprompt.reviewPrompt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.iwacchi.kakeibo.main.reviewPrompt.OPEN_REVIEWED
import com.iwacchi.kakeibo.main.reviewPrompt.REVIEWED
import com.iwacchi.kakeibo.main.reviewPrompt.ReviewCycleChecker
import com.iwacchi.reviewprompt.R
import com.iwacchi.reviewprompt.preferenceKey.PreferenceUtility

class ReviewPromptFragment : Fragment() {

    companion object {
        fun newInstance(): ReviewPromptFragment {
            return ReviewPromptFragment()
        }
    }

    private var negativeListener: View.OnClickListener? = null
    private lateinit var negativeButton: Button
    private lateinit var positiveButton: Button
    private lateinit var thanksButton: Button
    private lateinit var alreadyReviewedButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.review_prompt_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState != null) {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        negativeButton = view.findViewById(R.id.negative_button)
        positiveButton = view.findViewById(R.id.positive_button)
        thanksButton = view.findViewById(R.id.thanks_button)
        alreadyReviewedButton = view.findViewById(R.id.already_reviewed_button)

        // アプリレビュー画面に遷移しているかどうか
        if(! PreferenceUtility().getBoolean(OPEN_REVIEWED)) {
            alreadyReviewedButton.visibility = View.GONE
            negativeButton.visibility = View.VISIBLE
            negativeButton.setOnClickListener {
                // reviewサイクルを初期化、サイクル後に再度表示
                ReviewCycleChecker(PreferenceUtility()).resetReviewCycle()
                negativeListener?.onClick(it)
            }
        } else {
            negativeButton.visibility = View.GONE
            alreadyReviewedButton.visibility = View.VISIBLE
            alreadyReviewedButton.setOnClickListener {
                PreferenceUtility().putBoolean(REVIEWED, true)
                thanksMessage(view)
            }
        }
        positiveButton.setOnClickListener {
            openMyAppPlayStore()
            Thread(Runnable {
                Thread.sleep(500)
                Handler(Looper.getMainLooper()).post {
                    thanksMessage(view)
                }
            }).start()
        }
        thanksButton.setOnClickListener {
            negativeListener?.onClick(it)
        }
    }

    private fun thanksMessage(view: View) {
        view.findViewById<TextView>(R.id.title).text =
            getString(R.string.review_prompt_thank_you_message)
        view.findViewById<TextView>(R.id.description).text = ""
        thanksButton.visibility = View.VISIBLE
        positiveButton.visibility = View.GONE
        negativeButton.visibility = View.GONE
        alreadyReviewedButton.visibility = View.GONE
    }

    fun setNegativeListener(listener: View.OnClickListener) {
        negativeListener = listener
    }

    private fun openMyAppPlayStore() {
        PreferenceUtility().putBoolean(OPEN_REVIEWED, true)
        // 変更してください
        val id = "your app package"
        try{
            context?.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$id"))
            )
        } catch (_: android.content.ActivityNotFoundException) {
            context?.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$id"))
            )
        }
    }

}