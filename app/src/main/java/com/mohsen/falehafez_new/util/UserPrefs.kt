package com.mohsen.falehafez_new.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class UserPrefs (context: Context){

    private val FAVED_POEMS = "faved_poems"
    private lateinit var favedList: String

    private fun getSharedPreferences(context: Context) = context.getSharedPreferences(FAVED_POEMS, Context.MODE_PRIVATE)

    fun setFavedPoems(context: Context, index: Int) {
        favedList = getFavedPoems(context)
        favedList = favedList.plus("$index").plus(",")
        val editor = getSharedPreferences(context).edit()
        editor.putString("indexes",favedList)
        editor.apply()
    }
    fun getFavedPoems(context: Context): String {
        return getSharedPreferences(context).getString("indexes","")!!
    }

    fun getFavedPoemList(context: Context) : List<String> {
        var indexes: List<String> = ArrayList()
        indexes = getSharedPreferences(context).getString("indexes","")!!.split(",")
        Log.d("userPrefs","${indexes.size}")
         return indexes
    }
}


