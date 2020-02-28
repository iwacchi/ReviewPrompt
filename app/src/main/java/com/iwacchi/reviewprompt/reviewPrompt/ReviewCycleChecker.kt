package com.iwacchi.kakeibo.main.reviewPrompt

import android.util.Log
import com.iwacchi.reviewprompt.preferenceKey.PreferenceUtility
import java.text.SimpleDateFormat
import java.util.*

const val OPEN_REVIEWED = "open_reviewed"
const val REVIEWED = "reviewed"
private const val REVIEW_CYCLE = "review_cycle"
private const val CONTINUOUS_LOGIN = "continuous_login"

class ReviewCycleChecker(private val sharedPreferences: PreferenceUtility) {

    fun openAppCheck() {
        if(! sharedPreferences.getBoolean(REVIEWED)) {
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN)
            val prevOpenAppDate = sharedPreferences.getString(REVIEW_CYCLE)!!
            val prevCalendar = Calendar.getInstance()
            if (prevOpenAppDate != "") {
                prevCalendar.timeInMillis = format.parse(prevOpenAppDate)!!.time
                sharedPreferences.putString(REVIEW_CYCLE, format.format(calendar.time))
                val interval = diffDays(prevCalendar, calendar)
                if (interval == 1) {
                    var cycleCount = sharedPreferences.getInt(CONTINUOUS_LOGIN)
                    Log.i(
                        "review cycle up",
                        "prev: $prevOpenAppDate, update: ${format.format(calendar.time)}"
                    )
                    cycleCount++
                    Log.i(
                        "review count up",
                        "prev: ${cycleCount - 1}, update: $cycleCount"
                    )
                    sharedPreferences.putInt(CONTINUOUS_LOGIN, cycleCount)
                } else if (interval > 1) {
                    var cycleCount = sharedPreferences.getInt(CONTINUOUS_LOGIN)
                    Log.i(
                        "review count reset",
                        "prev: ${cycleCount}, update: 1"
                    )
                    cycleCount = 1
                    sharedPreferences.putInt(CONTINUOUS_LOGIN, cycleCount)
                }
            } else {
                Log.i(
                    "review cycle new count",
                    "set: ${format.format(calendar.time)}"
                )
                sharedPreferences.putString(REVIEW_CYCLE, format.format(calendar.time))
                sharedPreferences.putInt(CONTINUOUS_LOGIN, 1)
            }
        }
    }

    private fun diffDays(calendar1: Calendar, calendar2: Calendar): Int {
        val diffTime = calendar2.timeInMillis - calendar1.timeInMillis
        val millisOfDay = 1000 * 60 * 60 * 24
        return (diffTime / millisOfDay).toInt()
    }

    fun checkOpenReview(): Boolean {
        val count = sharedPreferences.getInt(CONTINUOUS_LOGIN)
        return count >= OPEN_REVIEW_INTERVAL
    }

    fun resetReviewCycle() {
        Log.i("reset review cycle","")
        sharedPreferences.putInt(CONTINUOUS_LOGIN, 1)
    }

    companion object {
        const val OPEN_REVIEW_INTERVAL = 3
    }

}