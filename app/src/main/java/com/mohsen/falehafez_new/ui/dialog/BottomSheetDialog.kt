package com.mohsen.falehafez_new.ui.dialog


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.util.px
import kotlin.math.abs


open class BottomSheetDialog(context: Context?, theme: Int) : AppCompatDialog(context, theme),
    View.OnTouchListener {

    lateinit var contentWrapper: ViewGroup

    var firstY = 0f
    var distance = 0f
    var firstViewY = 0f

    override fun setContentView(layoutResID: Int) {
        super.setContentView(wrapContentView(layoutResID, null, null))
    }

    override fun setContentView(view: View) {
        super.setContentView(wrapContentView(0, view, null))
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(wrapContentView(0, view, params))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }


    private fun wrapContentView(
        layoutResId: Int,
        view: View?,
        params: ViewGroup.LayoutParams?
    ): View {
        var contentView = view
        val container = View.inflate(
            this.context,
            R.layout.fragment_bottom_sheet,
            null as ViewGroup?
        ) as ViewGroup
        container.setOnClickListener {
            dismiss()
        }
        contentWrapper = container.findViewById<View>(R.id.hafez_fave_option) as ViewGroup
        contentWrapper.setOnTouchListener(this)
        if (layoutResId != 0 && contentView == null) {
            contentView = this.layoutInflater.inflate(layoutResId, contentWrapper, false)
        }

        if (params == null) {
            contentWrapper.addView(contentView)
        } else {
            contentWrapper.addView(contentView, params)
        }
        return container
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                firstY = event.rawY
                firstViewY = contentWrapper.y
            }
            MotionEvent.ACTION_MOVE -> {
                distance = event.rawY - firstY
                if (distance > 0)
                    contentWrapper.y = firstViewY + distance
                else
                    distance = 0f
            }
            else -> {
                if (abs(distance).toInt() > 200.px)
                    dismiss()
                else
                    contentWrapper.animate().y(firstViewY).start()

            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}