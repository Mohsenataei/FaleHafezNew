package com.mohsen.falehafez_new.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.navigation.NavController
import com.mohsen.falehafez_new.BaseActivity
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.ui.ui.faves.FavesFragment
import com.mohsen.falehafez_new.util.toast






class HomeActivity : BaseActivity() {
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val fragmentManager = supportFragmentManager
//        val favesFragment = FavesFragment()
//        when(item.itemId){
//            R.id.nav_share ->{
//                //toast("clicked on share button")
//                share()
//                return true
//            }
//            R.id.nav_faves-> {
////                fragmentManager.popBackStack()
////                fragmentManager.beginTransaction().replace(R.id.flContent,favesFragment).commit();
//            }
//
//            R.id.nav_rating ->{
//                //toast("هنوز پیاده سازی نشده است.")
//            }
//
//        }
//        return false
//    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_faves, R.id.nav_rating,R.id.nav_send
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
      //  navView.setNavigationItemSelectedListener(this)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun share(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        val body = "Your body here"
        val sub = "Your Subject"
        intent.putExtra(Intent.EXTRA_SUBJECT,sub);
        intent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(intent, "Share Using"))
    }




}
