package com.mohsen.falehafez_new.ui.ui.hafez


import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.PoemAdapter
import com.mohsen.falehafez_new.ui.HomeActivity
import com.mohsen.falehafez_new.util.AudioWifiLocal
import com.mohsen.falehafez_new.util.Constants.BASE_URL
import com.mohsen.falehafez_new.util.ResourceHelper
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
    var isPlaying = false

    var items: List<String> = ArrayList()
    var evaluate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {

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
        val rnd = genRanNum()

        ResourceHelper.getInstance(activity!!).getPoemData(activity!!,rnd)
        val items = ResourceHelper.getInstance(activity!!).getPoems()
        val evaluate= ResourceHelper.getInstance(activity!!).getEvaluate()
        val num = ResourceHelper.getInstance((activity!!)).getRandomNumber()
        poemFirstHemistich.text = items[0]
        poemTitle.text = "غزل شماره ${rnd+1}"
        Log.d("HafezFragment","onViewCreated invoked  ${items[0]}")

        nestedScrollView.setOnTouchListener { v, event ->
            topSection.dispatchTouchEvent(event)
        }

        PoemInterpretationTv.text = evaluate
        adapter = PoemAdapter(activity!!, items)

        poemRecycler.layoutManager = LinearLayoutManager(activity!!)

        poemRecycler.adapter = adapter
        poemFirstHemistich.setOnClickListener {
            (context as HomeActivity).navController.navigate(R.id.hafezFragment)
        }

//        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
//            setAudioStreamType(AudioManager.STREAM_MUSIC)
//            //setDataSource(activity!!, "")
//            prepare()
//            start()
//        }


        if (isPlaying == false){
            playPoemButton.setOnClickListener {
                playPoemButton.setImageResource(R.drawable.ic_pause_button)
                isPlaying = true
            }
        }
        if (isPlaying == true) {
            playPoemButton.setOnClickListener {
                playPoemButton.setImageResource(R.drawable.ic_play_button)
                isPlaying = false
            }
        }
        prepareForPlay()
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
    private fun stopPlayingPoem (){
        if (isPlaying == true){
            playPoemButton.setOnClickListener {
                playPoemButton.setImageResource(R.drawable.ic_play_button)
                isPlaying = false
            }

        }

    }

    private fun doplay(){
        val url = BASE_URL
        AudioWifiLocal.getInstance().release()
        AudioWifiLocal.getInstance()
            .init(context,Uri.parse(url))
            .setPlayView(playPoemButton)
            .setPauseView(pausePoemButton)
            .setSeekBar(appCompatSeekBar)
            .setAutoPlay(false)

    }
    override fun onStop() {
        super.onStop()
        AudioWifiLocal.getInstance().pause()
    }

    private fun prepareForPlay(){
        playPoemButton.setOnClickListener {
            doplay()
        }
    }

    private fun genRanNum(): Int{
        return (0..494).random()
    }


}
