package com.mohsen.falehafez_new.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.model.FavedPoem
import com.mohsen.falehafez_new.ui.ui.faves.FavesFragment
import java.util.*
import kotlin.collections.ArrayList

class ResourceHelper(context: Context) {

    //private val questionList = mutableListOf<IQQuestion>()
    private lateinit var randomPoem: String
    private lateinit var poem: List<String>
    private var rand: Int = 0


    companion object {
        private var instance: ResourceHelper? = null

        fun getInstance(context: Context): ResourceHelper {
            if (instance == null)
                instance = ResourceHelper(context)

            return instance as ResourceHelper
        }
    }

    init {
        //getData(context)
        genRandomNumber()
        getPoemData(context, getRandomNumber())
    }

    fun getPoemData(context: Context, index: Int) {
        val poemArray = context.resources.getStringArray(R.array.hafez)
        getPoemData(poemArray, index)
    }

    fun getPoemData(poemArray: Array<String>, index: Int) {
        Log.d("HafezFragment", "index is : $index")
        randomPoem = poemArray[index]
    }

    fun genRandomNumber() {
        rand = (0..494).random()
    }

    fun getRandomNumber(): Int {
        return rand
    }

    fun getPoems(): List<String> {
        val poems = randomPoem.substring(0, randomPoem.indexOf("n"))
        poem = poems.split("m")
        return poem
    }

    fun getEvaluate(): String {
        return randomPoem.substring(randomPoem.lastIndexOf("n") + 1)
    }

    fun getFirstLines(context: Context): List<FavedPoem> {
        var titles: MutableList<FavedPoem> = ArrayList()
        var title = ""
        val poemArray = context.resources.getStringArray(R.array.hafez)

        for (i in 0..494) {
            getPoemData(poemArray, i)
            title = randomPoem.substring(0, randomPoem.indexOf("m"))
            Log.d("allPoems", title)
            titles.add(FavedPoem(i,title))
        }
        return titles
    }

    fun searchInPoems(context: Context, query: String) : List<FavedPoem>{
        var results: MutableList<FavedPoem> = ArrayList()
        var result = ""
        val poemArray = context.resources.getStringArray(R.array.hafez)
        for (i in 0..494) {
            getPoemData(poemArray, i)
            result = randomPoem.substring(0, randomPoem.indexOf("n"))
            if (result.toLowerCase().contains(query.toLowerCase())){
                result = result.substring(0, result.indexOf("m"))
                Log.d("allPoems", result)
                results.add(FavedPoem(i,result))
            }
        }
        return results

    }


    fun getFavesList(context: Context, list: List<Int>): List<FavedPoem> {
        Log.d("getFavesList", "list size is : ${list.size}")
        var titles: MutableList<FavedPoem> = ArrayList()
        var title = ""
        val poemArray = context.resources.getStringArray(R.array.hafez)

        for (i in 0..list.size.minus(1)) {

            getPoemData(poemArray, list[i])
            title = randomPoem.substring(0, randomPoem.indexOf("m"))
            Log.d("getFavesList", title)
            titles.add(FavedPoem(list[i], title))

        }
        return titles
    }


    fun getSharePoemsIntent(): Intent {
        val poemItems=poem
        var message = "فال حافط \n\n"
        for (i in 0 until poemItems.size) {
            message += if (i % 2 == 0)
                poemItems[i] + "\n"
            else
                "\t\t\t\t\t" + poemItems[i]+"\n\n"
        }
        message+="تفسیر فال\n"
        message+=randomPoem.substring(randomPoem.lastIndexOf("n") + 1)+"\n"
        message+="\n برای دریافت اپلیکیشن هشدار از لینک زیر استفاده کنید"
        //message+="\n$APP_ADDRESS"
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
    }
}