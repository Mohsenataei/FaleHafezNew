package com.mohsen.falehafez_new.ui.ui.faves

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "شما هنوز هیچ فالی را به لیست علاقه مندی های خود اضافه نکرده اید."
    }
    val text: LiveData<String> = _text
}