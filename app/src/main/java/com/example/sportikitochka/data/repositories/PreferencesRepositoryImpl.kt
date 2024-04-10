package com.example.sportikitochka.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.sportikitochka.domain.repositories.PreferencesRepository

class PreferencesRepositoryImpl(
    private val context: Context,
) : PreferencesRepository {

    private val sharedPreferences by lazy {
        context.getSharedPreferences("app_preferences", 0)
    }

    private fun edit(block: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().apply {
            block()
        }.apply()
    }

    override fun putString(key: String, value: String) = edit {
        putString(key, value)
    }

    override fun getStringOrNull(key: String): String? = sharedPreferences.getString(key, null)
    override fun getString(key: String): String = sharedPreferences.getString(key,null).toString()

    override fun remove(key: String) = edit {
        remove(key)
    }

    override fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)


    override fun putBoolean(key: String, value: Boolean) =edit {
        putBoolean(key, value)
    }

    override fun putInt(key: String, value: Int) =edit {
        putInt(key, value)
    }

    override fun getInt(key: String): Int = sharedPreferences.getInt(key,0)

}