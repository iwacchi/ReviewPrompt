package com.iwacchi.reviewprompt.reviewPrompt

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.iwacchi.reviewprompt.reviewPrompt.ReviewPromptPagerAdapter

class ReviewPromptViewPager(context: Context,
                            attributeSet: AttributeSet) : ViewPager(context, attributeSet) {

    fun setAdapter(fragmentManager: FragmentManager) {
        this.adapter =
            ReviewPromptPagerAdapter(
                this,
                fragmentManager
            )
        this.currentItem = 1
        changeListener()
    }

    private fun changeListener() {
        this.addOnPageChangeListener(
            object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    when(position) {
                        0, 2 -> {
                            deleteReviewPrompt()
                        }
                    }
                }
            }
        )
    }

    fun deleteReviewPrompt() {
        val animation = ResizeAnimation(
            this,
            - this.height,
            this.height
        ).apply{
            duration = 200
        }
        Thread(Runnable {
            Thread.sleep(200)
            Handler(Looper.getMainLooper()).post {
                this.startAnimation(animation)
            }
            Thread.sleep(200)
            Handler(Looper.getMainLooper()).post {
                (this.parent as ViewGroup).removeView(this)
            }
        }).start()
    }

    class ResizeAnimation(private val view: View,
                          private val addHeight: Int,
                          private val startHeight: Int) : Animation() {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            val newHeight = startHeight + addHeight * interpolatedTime
            view.layoutParams.height = newHeight.toInt()
            view.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }

    }

}