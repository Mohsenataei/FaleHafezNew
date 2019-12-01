package com.mohsen.falehafez_new.util

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import android.net.ConnectivityManager
import android.net.NetworkInfo



fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}


// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition!!/1000
    }

fun convertFavedPoemIndexToString(indexes: IntArray): String{
    var tmp = ""
    for (index in indexes){
        tmp = tmp.plus("$index").plus(",")
    }
    Log.d("Convert",tmp)
    return tmp
}

val Number.px : Int
get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,toFloat(),Resources.getSystem().displayMetrics).toInt()


fun getConnectivityStatusString(context: Context): String? {
    var status: String? = null
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    if (activeNetwork != null) {
        if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
            status = "Wifi enabled"
            return status
        } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
            status = "Mobile data enabled"
            return status
        }
    } else {
        status = "No internet is available"
        return status
    }
    return status
}

fun checkConnection(context: Context): Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true

}
