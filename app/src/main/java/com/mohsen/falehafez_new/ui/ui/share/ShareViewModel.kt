package com.mohsen.falehafez_new.ui.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "اینجا صفحه اشتراک گذاری برنامه می باشد"
    }
    val text: LiveData<String> = _text
}