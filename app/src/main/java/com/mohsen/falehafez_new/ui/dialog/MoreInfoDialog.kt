package com.mohsen.falehafez_new.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.mohsen.falehafez_new.R
import kotlinx.android.synthetic.main.more_info_layout.*

class MoreInfoDialog (context: Context): Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.more_info_layout)
        with(window) {
            //this!!.setBackgroundDrawable(ColorDrawable(0))
            this!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }

        moreInfoDialogCloseBtn.setOnClickListener {
            dismiss()
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
////        return super.onCreateView(inflater, container, savedInstanceState)
//        return inflater.inflate(R.layout.more_info_layout, container);
//    }

}