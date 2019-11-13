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
        favedList = favedList.makeFavList().apply { add(index)}.concatFavList()
        val editor = getSharedPreferences(context).edit()
        editor.putString("indexes",favedList)
        editor.apply()
    }
    fun getFavedPoems(context: Context): String {
        return getSharedPreferences(context).getString("indexes","")!!
    }

    fun getFavedPoemList(context: Context) =
        getSharedPreferences(context).getString("indexes","")!!.split(",").map { it.toInt() }.also {
            Log.d("userPrefs","${it.size}")
        }

    fun String.makeFavList():MutableList<Int>{
        if(isBlank())
            return mutableListOf()
        return split(",").map { it.toInt() }.toMutableList().also {
            Log.d("userPrefs", "${it.size}")
        }

    }
    fun List<Int>.concatFavList()=joinToString(",")
}


