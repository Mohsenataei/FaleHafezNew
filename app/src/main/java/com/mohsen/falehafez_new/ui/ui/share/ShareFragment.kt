package com.mohsen.falehafez_new.ui.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mohsen.falehafez_new.R



class ShareFragment : Fragment() {

    private lateinit var shareViewModel: ShareViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shareViewModel =
            ViewModelProviders.of(this).get(ShareViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_share, container, false)
        val textView: TextView = root.findViewById(R.id.text_share)
        shareViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        val body = "Your body here"
        val sub = "Your Subject"
        intent.putExtra(Intent.EXTRA_SUBJECT,sub);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent, "Share Using"))
        
    }
}