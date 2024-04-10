package com.example.sportikitochka.domain.repositories

interface PreferencesRepository {
    fun putString(key: String, value: String)
    fun getStringOrNull(key: String): String?
    fun getString(key: String): String
    fun remove(key: String)

    fun getBoolean(key: String): Boolean
    fun putBoolean(key: String, value: Boolean)

    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int
}