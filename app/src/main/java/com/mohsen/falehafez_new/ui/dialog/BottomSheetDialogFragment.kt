package com.mohsen.falehafez_new.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.mohsen.falehafez_new.R


open class BottomSheetDialogFragment: AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(context!!, theme)
    }

    override fun onActivityCreated(arg: Bundle?) {
        super.onActivityCreated(arg)
      //  dialog!!.window!!.attributes.windowAnimations = R.style.Bottomsheet_Animation
    }
}