package com.intermediate.storyapp.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.intermediate.storyapp.data.response.LoginResult

object UserPreferences {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private const val PREFERENCE_NAME = "StoryApp.pref"

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_TOKEN = "user_token"

    val userName: String
        get() = preferences.getString(KEY_USER_NAME, "") ?: ""

    val userToken: String
        get() = preferences.getString(KEY_USER_TOKEN, "") ?: ""

    val userId: String
        get() = preferences.getString(KEY_USER_ID, "") ?: ""

    fun setLoginCredentials(loginResult: LoginResult) {
        editor.putString(KEY_USER_NAME, loginResult.name)
        editor.putString(KEY_USER_TOKEN, loginResult.token)
        editor.putString(KEY_USER_ID, loginResult.userId)
        editor.apply()
    }

    fun saveUserPreferences(user: LoginResult) {
        user.apply {
            editor.putString(KEY_USER_NAME, name)
            editor.putString(KEY_USER_ID, userId)
            editor.putString(KEY_USER_TOKEN, token)
            editor.apply()
        }
    }

    fun clearUserPreferences() {
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_TOKEN)
        editor.remove(KEY_USER_ID)
        editor.apply()
    }
}
