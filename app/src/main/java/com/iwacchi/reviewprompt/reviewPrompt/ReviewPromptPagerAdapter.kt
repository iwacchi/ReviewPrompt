package com.iwacchi.reviewprompt.reviewPrompt

import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.iwacchi.reviewprompt.reviewPrompt.ReviewPromptFragment

class ReviewPromptPagerAdapter(private val viewPager: ViewPager,
                               fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            1 -> {
                val fragment = ReviewPromptFragment.newInstance()
                fragment.setNegativeListener(
                    View.OnClickListener {
                        Thread(Runnable {
                            Thread.sleep(100)
                            Handler(Looper.getMainLooper()).post {
                                viewPager.setCurrentItem(0, true)
                            }
                        }).start()
                    }
                )
                fragment
            }
            else -> {
                Fragment()
            }
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return if(`object` is ReviewPromptFragment)
            PagerAdapter.POSITION_NONE
        else
            PagerAdapter.POSITION_UNCHANGED
    }

    override fun saveState(): Parcelable? {
        return null
    }

}