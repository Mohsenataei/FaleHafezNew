package com.mohsen.falehafez_new.ui.ui.hafez

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.falehafez_new.util.ResourceHelper

class HafezViewModel(context: Context) : ViewModel() {
    private var _list = MutableLiveData<List<String>>().apply {
        value = ResourceHelper.getInstance(context).getPoems()
    }

    val list: LiveData<List<String>> = _list


}