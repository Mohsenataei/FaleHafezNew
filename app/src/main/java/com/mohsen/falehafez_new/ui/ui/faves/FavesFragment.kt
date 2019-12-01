package com.mohsen.falehafez_new.ui.ui.faves

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.AllPoemAdapter
import com.mohsen.falehafez_new.adapter.FavedPoemAdapter
import com.mohsen.falehafez_new.model.FavedPoem
import com.mohsen.falehafez_new.util.ItemClickListener
import com.mohsen.falehafez_new.util.ResourceHelper
import com.mohsen.falehafez_new.util.UserPrefs
import com.mohsen.falehafez_new.util.toast
import kotlinx.android.synthetic.main.fragment_tools.*

class FavesFragment : Fragment(), ItemClickListener {
    lateinit var navController: NavController

    override fun onItemClicked(index: Int) {
        // val fragmentManager = activity!!.supportFragmentManager
        val bundle = bundleOf("index" to index.toString())
//        val hafezFragment = HafezFragment()
//        hafezFragment.poemIndex = index.toString()
//        fragmentManager.beginTransaction().replace(R.id.flContent,hafezFragment).commit()

        //context!!.toast("in faves fragment detected click")
        navController.navigate(R.id.action_nav_faves_to_hafezFragment, bundle)
    }

    private lateinit var toolsViewModel: FavesViewModel
    private lateinit var adapter: FavedPoemAdapter
    private lateinit var list: List<FavedPoem>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var favedPoems: List<Int>
    private lateinit var userPrefs: UserPrefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        toolsViewModel =
            ViewModelProviders.of(this).get(FavesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tools, container, false)
        val textView: TextView = root.findViewById(R.id.favesTV)
        toolsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        // navController = Navigation.findNavController(view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        userPrefs = UserPrefs(activity!!)
        favedPoems = userPrefs.getFavedPoemList(activity!!)

        val aaa: MutableList<Int> = ArrayList()
        for (item in favedPoems) {
            Log.d("Faves", item.toString())

            aaa.add(item)

        }


        if (favedPoems.isEmpty()) {
            favesTV.visibility = View.VISIBLE
        } else {
            ResourceHelper.getInstance(activity!!).getPoemData(activity!!, 0)
            list = ResourceHelper.getInstance(activity!!).getFavesList(activity!!, favedPoems)
            adapter = FavedPoemAdapter(activity!!, list, this)
            favesPoemsRecycler.layoutManager = LinearLayoutManager(activity!!)
            favesPoemsRecycler.adapter = adapter
        }
    }
}