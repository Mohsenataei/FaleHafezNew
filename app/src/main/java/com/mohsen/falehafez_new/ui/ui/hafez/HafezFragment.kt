package com.mohsen.falehafez_new.ui.ui.hafez


import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.PoemAdapter
import com.mohsen.falehafez_new.ui.HomeActivity
import com.mohsen.falehafez_new.util.*
import com.mohsen.falehafez_new.util.Constants.BASE_URL
import kotlinx.android.synthetic.main.fragment_hafez.*
import com.mohsen.falehafez_new.ui.ui.hafez.HafezViewModel as HafezViewModel1

/**
 * A simple [Fragment] subclass.
 */
class HafezFragment : Fragment() {

    lateinit var navController: NavController
    lateinit var adapter: PoemAdapter
    lateinit var hafezViewModel: HafezViewModel1
    lateinit var resourceHelper: ResourceHelper
    lateinit var seekBar: SeekBar
   // lateinit var mediaPlayer: MediaPlayer
    var isPlaying = false


    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false

    lateinit  var poemIndex: String

    var items: List<String> = ArrayList()
    var evaluate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        poemIndex = arguments!!.getString("index")!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //resourceHelper = ResourceHelper(activity!!)
       // HafezViewModel1 = ViewModelProviders.of(this).get(HafezViewModel1::class.java)

        val view = inflater.inflate(R.layout.fragment_hafez, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        seekBar = view.findViewById(R.id.appCompatSeekBar)
        val rnd = genRanNum()
        mediaPlayer = MediaPlayer.create(activity!!,R.raw.hafez_001)

        playPoemButton.setOnClickListener{
            context!!.toast("detect click")
            pausePoemButton.visibility = View.VISIBLE
            playPoemButton.visibility = View.GONE

            if(pause){
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
                context!!.toast("media playing")
            }else{

                mediaPlayer = MediaPlayer.create(activity!!,R.raw.hafez_001)
                mediaPlayer.start()
                context!!.toast("media playing")

            }
            initializeSeekBar()
            playPoemButton.isEnabled = false
            pausePoemButton.isEnabled = true
            stopPoemButton.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                playPoemButton.isEnabled = true
                pausePoemButton.isEnabled = false
                stopPoemButton.isEnabled = false
                context!!.toast("end")
            }
        }

        // Pause the media player
        pausePoemButton.setOnClickListener {
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                pause = true
                playPoemButton.isEnabled = true
                pausePoemButton.isEnabled = false
                stopPoemButton.isEnabled = true
                context!!.toast("media paused.")
            }
        }

        // Stop the media player
        stopPoemButton.setOnClickListener{
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            context!!.toast("media stoped.")
            if(mediaPlayer.isPlaying || pause.equals(true)){
                pause = false
                appCompatSeekBar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                playPoemButton.isEnabled = true
                pausePoemButton.isEnabled = false
                stopPoemButton.isEnabled = false
//                tv_pass.text = ""
//                tv_due.text = ""
                context!!.toast("media stoped.")
            }
        }

        // Seek bar change listener
        appCompatSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })




        if (poemIndex == "500"){
            context!!.toast("came from home")
            ResourceHelper.getInstance(activity!!).getPoemData(activity!!,rnd)
            items = ResourceHelper.getInstance(activity!!).getPoems()
            evaluate= ResourceHelper.getInstance(activity!!).getEvaluate()
            poemFirstHemistich.text = items[0]
            poemTitle.text = "غزل شماره ${rnd+1}"
        }else {
            context!!.toast("came from gallery")
            ResourceHelper.getInstance(activity!!).getPoemData(activity!!,poemIndex.toInt())
            items = ResourceHelper.getInstance(activity!!).getPoems()
            evaluate= ResourceHelper.getInstance(activity!!).getEvaluate()
            poemFirstHemistich.text = items[0]
            poemTitle.text = "غزل شماره ${poemIndex.toInt()+1}"
        }


//        poemFirstHemistich.text = items[0]
//        poemTitle.text = "غزل شماره ${rnd+1}"
        Log.d("HafezFragment","onViewCreated invoked  ${items[0]}")

//        nestedScrollView.setOnTouchListener { v, event ->
//            topSection.dispatchTouchEvent(event)
//        }

        PoemInterpretationTv.text = evaluate
        adapter = PoemAdapter(activity!!, items)

        poemRecycler.layoutManager = LinearLayoutManager(activity!!)

        poemRecycler.adapter = adapter
        poemFirstHemistich.setOnClickListener {
            (context as HomeActivity).navController.navigate(R.id.hafezFragment)
        }
    }


    private fun init(){
        topSection.doOnLayout {

        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
    }

    private fun playPoem(){
        if (isPlaying == false){
            playPoemButton.setOnClickListener {
                playPoemButton.setImageResource(R.drawable.ic_pause_button)
                isPlaying = true
            }
        } else {
            playPoemButton.setOnClickListener {
                playPoemButton.setImageResource(R.drawable.ic_play_button)
                isPlaying = false
            }
        }

    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }


    private fun genRanNum(): Int{
        return (0..494).random()
    }

    private fun initializeSeekBar() {
        appCompatSeekBar.max = mediaPlayer.seconds

        runnable = Runnable {
            appCompatSeekBar.progress = mediaPlayer.currentSeconds

//            tv_pass.text = "${mediaPlayer.currentSeconds} sec"
//            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
//            tv_due.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}

