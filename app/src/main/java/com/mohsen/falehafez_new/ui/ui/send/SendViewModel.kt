package com.mohsen.falehafez_new.ui.ui.send

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.falehafez_new.R
import kotlinx.android.synthetic.main.fragment_send.view.*

class SendViewModel(application: Application) : AndroidViewModel(application) {

    private val _text1 = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.text1)
    }
    val text1: LiveData<String> = _text1

     private val _text2 = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.text2)
    }
    val text2: LiveData<String> = _text2

     private val _text3 = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.text3)
    }
    val text3: LiveData<String> = _text3


     private val _text4 = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.text4)
    }
    val text4: LiveData<String> = _text4


     private val _text5 = MutableLiveData<String>().apply {
        value = getApplication<Application>().getString(R.string.text5)
    }
    val text5: LiveData<String> = _text5


}