package com.mohsen.falehafez_new.ui.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.ui.HomeActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

       val animation = AnimationUtils.loadAnimation(this, R.anim.slide_from_bottom)
        splashButton.animation = animation

        splashImg.animation =  AnimationUtils.loadAnimation(this, R.anim.slide_from_top)

        splashButton.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
}
