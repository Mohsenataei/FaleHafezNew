package com.mohsen.falehafez_new.ui.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mohsen.falehafez_new.R
import kotlinx.android.synthetic.main.fragment_home.*
import android.app.AlertDialog


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            //textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        hafezTomb.setOnClickListener {
            val bundle = bundleOf("index" to "500")
            navController.navigate(R.id.action_nav_home_to_hafezFragment,bundle)
        }

        moreInfo.setOnClickListener {
//            val fm = activity!!.supportFragmentManager
//            val moreInfoDialog = MoreInfoDialog(activity!!)
//            moreInfoDialog.show(fm,"")
//            val moreInfoDialog = MoreInfoDialog(activity!!)
//            moreInfoDialog.show()
            showMoreInfoDialog(R.layout.more_info_layout)

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.home_appbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.action_share ->{
                share()
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }

    }

    private fun share(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        val body = "سلام این برنامه رو نصب کنید جالبه"
        val sub = "https://www.google.com"
        intent.putExtra(Intent.EXTRA_SUBJECT,sub);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent, "اشتراک گذاری با..."))
    }

    private fun showMoreInfoDialog(layout:Int){

       val dialogBuilder = AlertDialog.Builder(activity!!)
        val layoutView = layoutInflater.inflate(layout,null)
        dialogBuilder.setView(layoutView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.attributes.windowAnimations = R.anim.slide_from_bottom
       // alertDialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show()
        layoutView.findViewById<TextView>(R.id.moreInfoDialogCloseBtn).setOnClickListener {
            alertDialog.dismiss()
        }

    }
}