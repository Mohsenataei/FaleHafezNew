package com.mohsen.falehafez_new.ui.ui.hafez


import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.core.view.get
import androidx.core.view.marginTop
import androidx.core.widget.NestedScrollView
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
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class HafezFragment : Fragment() {

    val array = intArrayOf(1, 2, 3, 4)
    val FILE_PREFIX = "Hafez - "
    val FILE_POSFIX = ".mp3"
    val BASE_URL = "http://185.128.80.34:8080/DivanHafezVoice/Hafez%20-%20"
    lateinit var navController: NavController
    lateinit var adapter: PoemAdapter
    lateinit var hafezViewModel: HafezViewModel1
    lateinit var resourceHelper: ResourceHelper
    lateinit var userPrefs: UserPrefs
    lateinit var faved: String
    lateinit var seekBar: SeekBar
    lateinit var fileUrl: String
    var index = 0
    // lateinit var mediaPlayer: MediaPlayer
    var isPlaying = false


    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false

    lateinit var poemIndex: String

    var items: List<String> = ArrayList()
    var evaluate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        userPrefs = UserPrefs(activity!!)
        faved = userPrefs.getFavedPoems(activity!!)
        convertFavedPoemIndexToString(array)
        seekBar = view.findViewById(R.id.appCompatSeekBar)
        val rnd = genRanNum()

        PoemInterpretationTv.doOnLayout {
            val height = PoemInterpretationTv.height
            val layoutParam =
                nestedScrollView.getChildAt(0).layoutParams as FrameLayout.LayoutParams
            layoutParam.topMargin = height
            nestedScrollView.getChildAt(0).layoutParams = layoutParam
            nestedScrollView.getChildAt(0).requestLayout()
        }
        if (poemIndex == "500") {
            //context!!.toast("came from home")
            ResourceHelper.getInstance(activity!!).getPoemData(activity!!, rnd)
            items = ResourceHelper.getInstance(activity!!).getPoems()
            evaluate = ResourceHelper.getInstance(activity!!).getEvaluate()
            poemFirstHemistich.text = items[0]
            poemTitle.text = "غزل شماره ${rnd + 1}"
            index = rnd
        } else {
            //   context!!.toast("came from gallery")
            ResourceHelper.getInstance(activity!!).getPoemData(activity!!, poemIndex.toInt())
            items = ResourceHelper.getInstance(activity!!).getPoems()
            evaluate = ResourceHelper.getInstance(activity!!).getEvaluate()
            poemFirstHemistich.text = items[0]
            poemTitle.text = "غزل شماره ${poemIndex.toInt() + 1}"
            index = poemIndex.toInt()
        }
        val trackNumber = createfileNum(index + 1)
        fileUrl = BASE_URL + trackNumber + FILE_POSFIX
        Log.d("tracknum", trackNumber)
        //val url = "http://dl.baranhits.ir/dl/music/98-08/Shadmehr_Aghili_Khaabe_Khosh.mp3"
//        Log.d("filename","index is ${index} and file name is $fileUrl")
//        mediaPlayer = MediaPlayer.create(activity!!,R.raw.hafez_001)
        mediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(fileUrl)
            prepare() // might take long! (for buffering, etc)


        }
        initializeSeekBar()

        play(fileUrl)

        Log.d("HafezFragment", "onViewCreated invoked  ${items[0]}")

        PoemInterpretationTv.text = evaluate
        adapter = PoemAdapter(activity!!, items)

        poemRecycler.layoutManager = LinearLayoutManager(activity)

        poemRecycler.adapter = adapter
        poemFirstHemistich.setOnClickListener {
            (context as HomeActivity).navController.navigate(R.id.hafezFragment)
        }
    }


    override fun onStop() {
        super.onStop()
        mediaPlayer.release()
    }


    private fun genRanNum(): Int {
        return (0..494).random()
    }

    private fun initializeSeekBar() {
        appCompatSeekBar.max = mediaPlayer?.duration / 1000

        runnable = Runnable {
            try {
                appCompatSeekBar!!.let {
                    it.progress = mediaPlayer.currentSeconds
                    handler.postDelayed(runnable, 1000)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: KotlinNullPointerException) {
                e.printStackTrace()
            }

        }
        handler.postDelayed(runnable, 1000)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        if (isFaved(index)) {
            //activity!!.toast("this poem is faved")
            inflater!!.inflate(R.menu.hafez_bookmarked, menu)
        } else {
            inflater!!.inflate(R.menu.hafez, menu)
            //    activity!!.toast("this poem is not faved")

        }

        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.hafez_fave_option -> {
                if (isFaved(index)) {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp)
                    Log.d("aaa", "removed item index :" + index.toString())
                    userPrefs.removeFromFaveList(activity!!, index)
                    Log.d("prefs", userPrefs.getFavedPoems(activity!!))
                    //  context!!.toast("poem removed from faved list")
                } else {
                    item.setIcon(R.drawable.ic_favorite_black_24dp)
                    userPrefs.setFavedPoems(activity!!, index)
                    Log.d("prefs", userPrefs.getFavedPoems(activity!!))
                    // context!!.toast("added to list")
                }
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        }

    }


    private fun isFaved(index: Int): Boolean {
        val faves = userPrefs.getFavedPoemList(activity!!)
        if (faves.contains(index))
            return true
        return false
    }

    private fun createfileNum(index: Int): String {
        if (index < 10) {
            return "00".plus(index.toString())
            Log.d("filename", "00".plus(index.toString()))
        } else if (index in 10..99) {
            return "0".plus(index.toString())
            Log.d("filename", "0".plus(index.toString()))
        }
        Log.d("filename", (index.toString()))
        return index.toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        //  stopPoem()
    }
    override fun onResume() {
        super.onResume()
        activity!!.toast("onResume invoked.")
        if (pause){
            pause = false
            playPoemButton.visibility = View.VISIBLE
            pausePoemButton.visibility = View.GONE

            playPoemButton.isEnabled = true
            pausePoemButton.isEnabled = false
            stopPoemButton.isEnabled = false
        }
    }
    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
//        pausePoemButton.visibility = View.GONE
//        playPoemButton.visibility = View.VISIBLE

    }



    private fun play(url: String) {

        playPoemButton.setOnClickListener {
            context!!.toast("detect click on play")
            if (checkConnection(activity!!)) {
                //context!!.toast("network status: " + checkConnection(activity!!).toString())
                playPoemButton.visibility = View.GONE
                pausePoemButton.visibility = View.VISIBLE
                //initializeSeekBar()

                if (pause) {
                    mediaPlayer.seekTo(mediaPlayer.currentPosition)
                    mediaPlayer.start()
                    pause = !pause
                    // context!!.toast("media playing")
                } else {
//                mediaPlayer = MediaPlayer.create(activity!!,R.raw.hafez_001)
//                mediaPlayer.start()
                    mediaPlayer = MediaPlayer().apply {
                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                        setDataSource(url)
                        prepare() // might take long! (for buffering, etc)
                        start()
                    }
                }
                playPoemButton.isEnabled = false
                pausePoemButton.isEnabled = true
                stopPoemButton.isEnabled = true

                mediaPlayer.setOnCompletionListener {
                    playPoemButton.isEnabled = true
                    pausePoemButton.isEnabled = false
                    stopPoemButton.isEnabled = false
                    //context!!.toast("setOnCompletionListener end")
                }
            } else {
                // activity!!.toast("network not connected.")
            }

        }

        // Pause the media player
        pausePoemButton.setOnClickListener {
            context!!.toast("detect click on pause")
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                playPoemButton.isEnabled = true
                pausePoemButton.isEnabled = false
                stopPoemButton.isEnabled = true
                //context!!.toast("media paused.")
            }
        }

        // Stop the media player
        stopPoemButton.setOnClickListener {
            context!!.toast("detect click on stop")
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            // context!!.toast("media stoped.")
            try {
                if (mediaPlayer.isPlaying || pause.equals(true)) {
                    pause = false
                    // appCompatSeekBar.setProgress(0)
                    appCompatSeekBar.progress = 0
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.release()
                    handler.removeCallbacks(runnable)

                    playPoemButton.isEnabled = true
                    pausePoemButton.isEnabled = false
                    stopPoemButton.isEnabled = false
//                tv_pass.text = ""
//                tv_due.text = ""
                    // context!!.toast("media stoped.")
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }

        // Seek bar change listener
//        playPoem()
//        pausePoem()
//        stopPoem()
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
    }


    private fun playPoem() {
        playPoemButton.setOnClickListener {
            //  context!!.toast("detect click")
            if (checkConnection(activity!!)) {
                //context!!.toast("network status: " + checkConnection(activity!!).toString())
                playPoemButton.visibility = View.GONE
                pausePoemButton.visibility = View.VISIBLE
                //initializeSeekBar()

                if (pause) {
                    mediaPlayer.seekTo(mediaPlayer.currentPosition)
                    mediaPlayer.start()
                    pause = !pause
                    // context!!.toast("media playing")
                } else {
//                mediaPlayer = MediaPlayer.create(activity!!,R.raw.hafez_001)
//                mediaPlayer.start()
                    mediaPlayer = MediaPlayer().apply {
                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                        setDataSource(fileUrl)
                        prepare() // might take long! (for buffering, etc)
                        start()
                    }
                }
                playPoemButton.isEnabled = false
                pausePoemButton.isEnabled = true
                stopPoemButton.isEnabled = true

                mediaPlayer.setOnCompletionListener {
                    playPoemButton.isEnabled = true
                    pausePoemButton.isEnabled = false
                    stopPoemButton.isEnabled = false
                    //context!!.toast("setOnCompletionListener end")
                }
            } else {
                // activity!!.toast("network not connected.")
            }

        }
    }

    private fun pausePoem() {
        pausePoemButton.setOnClickListener {
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                playPoemButton.isEnabled = true
                pausePoemButton.isEnabled = false
                stopPoemButton.isEnabled = true
                //context!!.toast("media paused.")
            }
        }
    }

    private fun stopPoem() {
        stopPoemButton.setOnClickListener {
            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            // context!!.toast("media stoped.")
            try {
                if (mediaPlayer.isPlaying || pause.equals(true)) {
                    pause = false
                    // appCompatSeekBar.setProgress(0)
                    appCompatSeekBar.progress = 0
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.release()
                    handler.removeCallbacks(runnable)

                    playPoemButton.isEnabled = true
                    pausePoemButton.isEnabled = false
                    stopPoemButton.isEnabled = false
//                tv_pass.text = ""
//                tv_due.text = ""
                    // context!!.toast("media stoped.")
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }


}

