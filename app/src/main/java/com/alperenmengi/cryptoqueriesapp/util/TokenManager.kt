package com.alperenmengi.cryptoqueriesapp.util

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveAccessToken(context: Context, token: String) {
        getPrefs(context).edit().apply {
            putString(KEY_ACCESS_TOKEN, token)
            apply()
        }
    }

    fun saveRefreshToken(context: Context, token: String) {
        getPrefs(context).edit().apply {
            putString(KEY_REFRESH_TOKEN, token)
            apply()
        }
    }

    fun getRefreshToken(context: Context): String? {
        return getPrefs(context).getString(KEY_REFRESH_TOKEN, null)
    }

    fun getAccessToken(context: Context): String? {
        return getPrefs(context).getString(KEY_ACCESS_TOKEN, null)
    }

    fun clearAccessToken(context: Context) {
        getPrefs(context).edit().apply {
            remove(KEY_ACCESS_TOKEN)
            apply()
        }
    }

    fun clearRefreshToken(context: Context) {
        getPrefs(context).edit().apply {
            remove(KEY_REFRESH_TOKEN)
            apply()
        }
    }
} 