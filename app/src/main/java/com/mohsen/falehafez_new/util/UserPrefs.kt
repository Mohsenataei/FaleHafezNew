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

    fun getFavedPoemList(context: Context) :List<Int> {
      //  var result = List<Int>()
        val temp = getSharedPreferences(context).getString("indexes","")!!
        if (temp == ""){
            return emptyList()
        }else {
            return temp.split(",").map {
                it.toInt()
            }.also {
                Log.d("userPrefs","faved poems are : ${it}")
            }
        }
    }



    fun removeFromFaveList(context: Context,index:Int) {
        val list = getFavedPoemList(context) as MutableList
        Log.d("userPrefs", "list before delete any item : " + list.toString())

        for (item in list) {
            if (item == index) {
                Log.d("userPrefs", "removed item : " + index.toString())
                list.remove(index)
                Log.d("userPrefs", "list contains : " + list.toString())
                break
            }
        }

        Log.d("userPrefs", "list after deleting item " + list.concatFavList() )
        favedList = list.concatFavList()
        val editor = getSharedPreferences(context).edit()
        editor.putString("indexes",favedList)
        editor.apply()
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


