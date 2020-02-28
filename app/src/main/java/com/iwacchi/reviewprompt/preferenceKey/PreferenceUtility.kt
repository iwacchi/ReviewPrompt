package com.iwacchi.reviewprompt.preferenceKey

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable

class PreferenceUtility {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
    }

    fun setSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    private fun getEditor(): SharedPreferences.Editor {
        editor = sharedPreferences.edit()
        return editor
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun putString(key: String, value: String) {
        getEditor().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        getEditor().putBoolean(key, value).apply()
    }

    fun putInt(key: String, value: Int){
        getEditor().putInt(key, value).apply()
    }
}