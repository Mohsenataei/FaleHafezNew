package com.mohsen.falehafez_new.ui.ui.gallery

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.AllPoemAdapter
import com.mohsen.falehafez_new.util.ItemClickListener
import com.mohsen.falehafez_new.util.ResourceHelper
import kotlinx.android.synthetic.main.fragment_gallery.*
import androidx.core.view.MenuItemCompat
import com.mohsen.falehafez_new.ui.HomeActivity
import android.graphics.Typeface
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView


class GalleryFragment : Fragment(), ItemClickListener {

    lateinit var navController: NavController

    override fun onItemClicked(index: Int) {
       // activity!!.toast("clicked on row ${index+1}")
        val bundle = bundleOf("index" to index.toString())
      //  bundle.putInt("index",index)
        navController.navigate(R.id.action_nav_all_to_hafezFragment,bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.gallery_appbar,menu)

        val item = menu!!.findItem(R.id.search)
        val searchView =
            SearchView((context as HomeActivity).supportActionBar!!.themedContext)

        MenuItemCompat.setShowAsAction(
            item,
            MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItemCompat.SHOW_AS_ACTION_IF_ROOM
        )
        MenuItemCompat.setActionView(item, searchView)
        searchView.gravity = Gravity.RIGHT
        val iranSansFont = Typeface.createFromAsset(context!!.applicationContext.assets, "fonts/iran_sans_normal.ttf")
//        val id =
//            searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
//        val searchText = searchView.findViewById<View>(id) as TextView
      //  searchText.typeface = iranSansFont
        searchView.queryHint = "جست و جو کنید"
        searchView.findViewById<TextView>(R.id.search_src_text).typeface = iranSansFont
        searchView.findViewById<TextView>(R.id.search_src_text).gravity = Gravity.RIGHT

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                ResourceHelper.getInstance(activity!!).getPoemData(activity!!,0)
                val list = ResourceHelper.getInstance(activity!!).searchInPoems(activity!!,query)
                val newAdapter = AllPoemAdapter(activity!!,list,this@GalleryFragment)
                allPoemsRecycler.swapAdapter(newAdapter,false)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnClickListener {

        }
        val closeButton = searchView.findViewById<AppCompatImageView>(R.id.search_close_btn)
        closeButton.setOnClickListener {
            allPoemsRecycler.swapAdapter(adapter,false)
            searchView.findViewById<TextView>(R.id.search_src_text).text = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        //return super.onOptionsItemSelected(item)
        if (item!!.itemId == R.id.search){

        }
        return true
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