package com.mohsen.falehafez_new.util

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast

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
        return this.currentPosition/1000
    }