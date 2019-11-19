package com.mohsen.falehafez_new.ui.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mohsen.falehafez_new.R

class SendFragment : Fragment() {

    private lateinit var sendViewModel: SendViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(SendViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_send, container, false)
        val text1: TextView = root.findViewById(R.id.text1)
        sendViewModel.text1.observe(this, Observer {
            text1.text = it
        })
        val text2: TextView = root.findViewById(R.id.text2)
        sendViewModel.text2.observe(this, Observer {
            text2.text = it
        })
        val text3: TextView = root.findViewById(R.id.text3)
        sendViewModel.text3.observe(this, Observer {
            text3.text = it
        })

        val text4: TextView = root.findViewById(R.id.text4)
        sendViewModel.text4.observe(this, Observer {
            text4.text = it
        })

        val text5: TextView = root.findViewById(R.id.text5)
        sendViewModel.text5.observe(this, Observer {
            text5.text = it
        })







        return root
    }

    private fun setTextViews(viewmodel: SendViewModel){

    }
}