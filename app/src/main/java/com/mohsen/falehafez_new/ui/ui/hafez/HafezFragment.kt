package com.mohsen.falehafez_new.ui.ui.hafez


import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.core.view.doOnLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohsen.falehafez_new.R
import com.mohsen.falehafez_new.adapter.PoemAdapter
import com.mohsen.falehafez_new.ui.HomeActivity
import com.mohsen.falehafez_new.util.*
import kotlinx.android.synthetic.main.fragment_hafez.*
import com.mohsen.falehafez_new.util.MediaPlayerService
import android.content.Context
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.R.attr.name
import java.lang.reflect.Array.getLength
import android.content.res.AssetFileDescriptor
import androidx.core.content.ContextCompat
import android.R.attr.name
import android.media.AudioManager
import android.widget.Toast


/**
 * A simple [Fragment] subclass.
 */
class HafezFragment : Fragment(), Runnable {

    val array = intArrayOf(1, 2, 3, 4)
    val FILE_POSFIX = ".mp3"
    val BASE_URL = "http://185.128.80.34:8080/DivanHafezVoice/Hafez%20-%20"
    lateinit var navController: NavController
    lateinit var adapter: PoemAdapter
    lateinit var userPrefs: UserPrefs
    lateinit var faved: String
    // lateinit var seekBar: SeekBar
    lateinit var fileUrl: String
    var index = 0

    //private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false

    lateinit var poemIndex: String

    var items: List<String> = ArrayList()
    var evaluate: String? = null

    private lateinit var player: MediaPlayerService
    private var serviceBound: Boolean = false

    // new way to play audio variables :

    //lateinit var mediaPlayer = MediaPlayer()
    private var mediaPlayer: MediaPlayer? = null
    var seekBar: SeekBar? = null
    var wasPlaying = false
    var fab: FloatingActionButton? = null


    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false;
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        poemIndex = arguments!!.getString("index")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_hafez, container, false)
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
        Log.d("HafezFragment", "onViewCreated invoked  ${items[0]}")
        PoemInterpretationTv.text = evaluate
        adapter = PoemAdapter(activity!!, items)

        poemRecycler.layoutManager = LinearLayoutManager(activity)

        poemRecycler.adapter = adapter
        poemFirstHemistich.setOnClickListener {
            (context as HomeActivity).navController.navigate(R.id.hafezFragment)
        }

        sharePoemButton.setOnClickListener {
            val sendIntent = ResourceHelper.getInstance(activity!!).getSharePoemsIntent()
            startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
        }

        playPoemButton.setOnClickListener {
            //playAudio(fileUrl)
            playPoem1()
        }
        pausePoemButton.setOnClickListener {
            pausePoem1()
        }

        stopPoemButton.setOnClickListener {
            stopPoem()
        }
        appCompatSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    mediaPlayer?.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    mediaPlayer?.seekTo(seekBar!!.progress)
                }
            }

        })

    }

    private fun pausePoem() {
        try {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                clearMediaPlayer()
                appCompatSeekBar?.progress = 0
                wasPlaying = true
                playPoemButton.visibility = View.GONE
                pausePoemButton.visibility = View.VISIBLE
            }

            if (wasPlaying) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }
                playPoemButton.visibility = View.VISIBLE
                pausePoemButton.visibility = View.GONE
                mediaPlayer?.pause()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playPoem() {
        try {

            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                clearMediaPlayer()
                appCompatSeekBar?.progress = 0
                wasPlaying = true
                playPoemButton.visibility = View.GONE
                pausePoemButton.visibility = View.VISIBLE
            }


            if (!wasPlaying) {

                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }

                playPoemButton.visibility = View.GONE
                pausePoemButton.visibility = View.VISIBLE


                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer?.setDataSource(fileUrl)
                mediaPlayer?.prepare()
                //  mediaPlayer?.setVolume(0.5f, 0.5f)
                mediaPlayer?.isLooping = false
                appCompatSeekBar?.max = mediaPlayer!!.duration

                mediaPlayer?.start()
                Thread(this).start()

            }

            wasPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun clearMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun doPlay(url: String, autoPlay: Boolean) {

        AudioWifiLocal.getInstance().release()

        AudioWifiLocal.getInstance()
            .init(activity, Uri.parse(url))
            .setPlayView(playPoemButton)
            .setPauseView(pausePoemButton)
            .setSeekBar(appCompatSeekBar)
            .setAutoPlay(autoPlay)
            .setRuntimeView(poemTotalTime)
            .setTotalTimeView(poemTotalTime)

    }


    private fun genRanNum(): Int {
        return (0..494).random()
    }

    private fun initializeSeekBar() {
        appCompatSeekBar.max = mediaPlayer!!.duration / 1000

        runnable = Runnable {
            try {
                appCompatSeekBar!!.let {
                    it.progress = mediaPlayer!!.currentSeconds
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
            Log.d("filename", "00".plus(index.toString()))
            return "00".plus(index.toString())
        } else if (index in 10..99) {
            Log.d("filename", "0".plus(index.toString()))
            return "0".plus(index.toString())
        }
        Log.d("filename", (index.toString()))
        return index.toString()
    }


    override fun onPause() {
        super.onPause()
        AudioWifiLocal.getInstance().pause()
    }


    override fun onDestroy() {
        super.onDestroy()
        // activity!!.toast("ondestroy")
        //AudioWifiLocal.getInstance().release()
        clearMediaPlayer();
    }

    private fun playAudio(media: String) {
        //Check is service is active
        if (!serviceBound) {
            val playerIntent = Intent(activity, MediaPlayerService::class.java)
            playerIntent.putExtra("media", media)
            activity?.startService(playerIntent)
            activity?.bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }

    }

    override fun run() {

        var currentPosition = mediaPlayer?.currentPosition
        val total = mediaPlayer?.duration


        while (mediaPlayer != null && mediaPlayer!!.isPlaying && currentPosition!! < total!!) {
            try {
                Thread.sleep(1000);
                currentPosition = mediaPlayer?.currentPosition
            } catch (e: InterruptedException) {
                return
            } catch (e: Exception) {
                return
            }

            appCompatSeekBar?.progress = currentPosition!!

        }
    }


    private fun playPoem1() {
        appCompatSeekBar.visibility = View.VISIBLE
        if (pause) {
            mediaPlayer?.seekTo(mediaPlayer!!.currentPosition)
            mediaPlayer?.start()
            pause = false
            Toast.makeText(activity, "media playing", Toast.LENGTH_SHORT).show()
        } else {

            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.setDataSource(fileUrl)
            mediaPlayer?.prepare()
            //  mediaPlayer?.setVolume(0.5f, 0.5f)
            mediaPlayer?.isLooping = false
            appCompatSeekBar?.max = mediaPlayer!!.duration

            mediaPlayer?.start()
            Toast.makeText(activity, "media playing", Toast.LENGTH_SHORT).show()

        }
        initializeSeekBar()
        playPoemButton.visibility = View.GONE
        pausePoemButton.visibility = View.VISIBLE
        playPoemButton.isEnabled = false
        pausePoemButton.isEnabled = true
        stopPoemButton.isEnabled = true

        mediaPlayer?.setOnCompletionListener {
            playPoemButton.isEnabled = true
            playPoemButton.visibility = View.VISIBLE
            pausePoemButton.visibility = View.GONE
            pausePoemButton.isEnabled = false
            stopPoemButton.isEnabled = false
            Toast.makeText(activity, "end", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pausePoem1() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            pause = true
            playPoemButton.visibility = View.VISIBLE
            pausePoemButton.visibility = View.GONE
            playPoemButton.isEnabled = true
            pausePoemButton.isEnabled = false
            stopPoemButton.isEnabled = true
            Toast.makeText(activity, "media pause", Toast.LENGTH_SHORT).show()
        }
    }


    private fun stopPoem() {
        appCompatSeekBar.visibility = View.INVISIBLE
        if (mediaPlayer!!.isPlaying || pause.equals(true)) {
            pause = false
            appCompatSeekBar.progress = 0
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            handler.removeCallbacks(runnable)

            pausePoemButton.visibility = View.GONE
            playPoemButton.visibility = View.VISIBLE
            playPoemButton.isEnabled = true
            pausePoemButton.isEnabled = false
            stopPoemButton.isEnabled = false

            Toast.makeText(activity, "media stop", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initializeSeekBar1() {
        appCompatSeekBar.max = mediaPlayer!!.seconds

        runnable = Runnable {
            appCompatSeekBar?.progress = mediaPlayer!!.currentSeconds

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}

