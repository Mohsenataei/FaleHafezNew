package com.mohsen.falehafez_new.ui.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.AllPoemAdapter
import com.mohsen.falehafez_new.util.ItemClickListener
import com.mohsen.falehafez_new.util.ResourceHelper
import com.mohsen.falehafez_new.util.toast
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment(), ItemClickListener {

    lateinit var navController: NavController

    override fun onItemClicked(index: Int) {
        activity!!.toast("clicked on row ${index+1}")
        val bundle = bundleOf("index" to index.toString())
      //  bundle.putInt("index",index)
        navController.navigate(R.id.action_nav_all_to_hafezFragment,bundle)
    }

    private lateinit var galleryViewModel: GalleryViewModel
    lateinit var adapter: AllPoemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
      //  val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(this, Observer {
           // textView.text = it
        })
//        navController = root.findNavController()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        ResourceHelper.getInstance(activity!!).getPoemData(activity!!,0)
        val list = ResourceHelper.getInstance(activity!!).getFirstLines(activity!!)
        adapter = AllPoemAdapter(activity!!,list,this)
        allPoemsRecycler.layoutManager = LinearLayoutManager(activity!!)
        allPoemsRecycler.adapter = adapter
    }
}